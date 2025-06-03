package song.mygg1.domain.riot.service.league;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import song.mygg1.domain.redis.service.CacheService;
import song.mygg1.domain.riot.dto.league.LeagueEntryDto;
import song.mygg1.domain.riot.entity.league.LeagueEntry;
import song.mygg1.domain.riot.mapper.league.LeagueEntryMapper;
import song.mygg1.domain.riot.repository.league.LeagueEntryJpaRepository;
import song.mygg1.domain.riot.service.ApiService;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LeagueService {
    private final CacheService<Set<LeagueEntryDto>> leagueEntryCacheService;
    private final LeagueEntryJpaRepository leagueEntryRepository;
    private final ApiService apiService;
    private final LeagueEntryMapper leagueEntryMapper;

    private static final Duration LEAGUE_ENTRY_TTL = Duration.ofHours(1);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Set<LeagueEntryDto> getLeague(String puuid) {
        String key = "league:entry:puuid:" + puuid;

        return leagueEntryCacheService.getOrLoad(
                key,
                () -> {
                    Set<LeagueEntryDto> leagueEntrySet = leagueEntryRepository.findLeagueEntriesById_Puuid(puuid).stream()
                            .map(leagueEntryMapper::toDto).collect(Collectors.toSet());

                    if (leagueEntrySet.isEmpty()) {
                        log.info("League entries not found in DB puuid: {}", puuid);
                        Set<LeagueEntryDto> apiLeagueEntryDtoSet = apiService.getLeagueEntry(puuid);

                        if (apiLeagueEntryDtoSet.isEmpty()) {
                            log.info("No league entries found from API puuid: {}.", puuid);
                            return Collections.emptySet();
                        }

                        List<LeagueEntry> apiLeagueEntryEntities = apiLeagueEntryDtoSet.stream()
                                .map(leagueEntryMapper::toEntity).toList();

                        List<LeagueEntry> saved = leagueEntryRepository.saveAll(apiLeagueEntryEntities);
                        log.info("Save {} league entries from API for puuid: {}", saved.size(), puuid);
                        return new HashSet<>(saved.stream().map(leagueEntryMapper::toDto).toList());
                    }
                    log.debug("Found {} league entries in DB for puuid: {}", leagueEntrySet.size(), puuid);
                    return leagueEntrySet;
                },
                LEAGUE_ENTRY_TTL
        );
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Set<LeagueEntryDto> refreshLeague(String puuid) {
        log.info("Refreshing league entries puuid: {}", puuid);
        Set<LeagueEntry> puuidLeagueSet = leagueEntryRepository.findLeagueEntriesById_Puuid(puuid);
        Map<String, LeagueEntry> existingByQueue = puuidLeagueSet.stream()
                .collect(Collectors.toMap(e -> e.getId().getQueueType(), e -> e));

        Set<LeagueEntryDto> dtoSetFromApi = apiService.getLeagueEntry(puuid);

        List<LeagueEntry> toSave = new ArrayList<>();

        for (LeagueEntryDto dto : dtoSetFromApi) {
            String queue = dto.getQueueType();
            LeagueEntry entry = existingByQueue.remove(queue);
            if (entry != null) {
                log.debug("Updating existing league entry puuid: {}, queue: {}", puuid, queue);
                entry.update(dto.getLeagueId(), dto.getTier(), dto.getRank(), dto.getSummonerId(), dto.getLeaguePoints(), dto.getWins(), dto.getLosses(), dto.isHotStreak(), dto.isVeteran(), dto.isFreshBlood(), dto.isInactive());
            } else {
                log.debug("Creating new league entry puuid: {}, queue: {}", puuid, queue);
                entry = LeagueEntry.create(dto.getQueueType(), dto.getPuuid(), dto.getLeagueId(), dto.getTier(), dto.getRank(), dto.getSummonerId(), dto.getLeaguePoints(),
                        dto.getWins(), dto.getLosses(), dto.isHotStreak(), dto.isVeteran(), dto.isFreshBlood(), dto.isInactive());
            }
            toSave.add(entry);
        }

        List<LeagueEntry> toDelete = new ArrayList<>(existingByQueue.values());

        if (!toDelete.isEmpty()) {
            log.info("Deleting {} league entries puuid: {}", toDelete.size(), puuid);
            leagueEntryRepository.deleteAll(toDelete);
        }

        List<LeagueEntry> savedOrUpdatedEntries = leagueEntryRepository.saveAll(toSave);
        log.info("Saved/Updated {} league entries puuid: {}", savedOrUpdatedEntries.size(), puuid);

        Set<LeagueEntryDto> resultSet = savedOrUpdatedEntries.stream()
                .map(leagueEntryMapper::toDto)
                .collect(Collectors.toSet());

        String cacheKey = "league:entry:puuid:" + puuid;
        leagueEntryCacheService.put(cacheKey, resultSet, LEAGUE_ENTRY_TTL);
        log.info("League entry cache updated key: {}", cacheKey);

        return resultSet;
    }
}
