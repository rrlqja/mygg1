package song.mygg1.domain.riot.service.league;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import song.mygg1.domain.common.exception.riot.league.exceptions.LeagueListNotFoundException;
import song.mygg1.domain.redis.service.CacheService;
import song.mygg1.domain.riot.dto.league.LeagueEntryDto;
import song.mygg1.domain.riot.dto.league.LeagueListDto;
import song.mygg1.domain.riot.entity.league.LeagueEntry;
import song.mygg1.domain.riot.entity.league.LeagueList;
import song.mygg1.domain.riot.entity.league.LeagueQueue;
import song.mygg1.domain.riot.mapper.league.LeagueEntryMapper;
import song.mygg1.domain.riot.mapper.league.LeagueListMapper;
import song.mygg1.domain.riot.repository.league.LeagueEntryJpaRepository;
import song.mygg1.domain.riot.repository.league.LeagueListJpaRepository;
import song.mygg1.domain.riot.service.ApiService;

import java.time.Duration;
import java.util.ArrayList;
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
    private final CacheService<LeagueListDto> leagueListCacheService;
    private final LeagueEntryJpaRepository leagueEntryRepository;
    private final LeagueListJpaRepository leagueListRepository;
    private final ApiService apiService;
    private final LeagueEntryMapper leagueEntryMapper;
    private final LeagueListMapper leagueListMapper;

    private static final Duration LEAGUE_ENTRY_TTL = Duration.ofHours(1);
    private static final Duration LEAGUE_LIST_TTL = Duration.ofHours(24);

    @Transactional
    public Set<LeagueEntryDto> getLeague(String puuid) {
        String key = "league:entry:puuid:" + puuid;

        return leagueEntryCacheService.getOrLoad(
                key,
                () -> {
                    Set<LeagueEntryDto> leagueEntrySet = leagueEntryRepository.findLeagueEntriesById_Puuid(puuid).stream()
                            .map(leagueEntryMapper::toDto).collect(Collectors.toSet());

                    if (leagueEntrySet.isEmpty()) {
                        Set<LeagueEntryDto> apiLeagueEntryDtoSet = apiService.getLeagueEntry(puuid);

                        List<LeagueEntry> apiLeagueEntrySet = apiLeagueEntryDtoSet.stream()
                                .map(leagueEntryMapper::toEntity).toList();

                        List<LeagueEntry> saved = leagueEntryRepository.saveAll(apiLeagueEntrySet);
                        return new HashSet<>(saved.stream().map(leagueEntryMapper::toDto).toList());
                    }

                    return leagueEntrySet;
                },
                LEAGUE_ENTRY_TTL
        );
    }

    @Transactional
    public Set<LeagueEntryDto> refreshLeague(String puuid) {
        Set<LeagueEntry> puuidLeagueSet = leagueEntryRepository.findLeagueEntriesById_Puuid(puuid);
        Map<String, LeagueEntry> existingByQueue = puuidLeagueSet.stream()
                .collect(Collectors.toMap(e -> e.getId().getQueueType(), e -> e));

        Set<LeagueEntryDto> dtoSet = apiService.getLeagueEntry(puuid);

        List<LeagueEntry> toSave = new ArrayList<>();

        for (LeagueEntryDto dto : dtoSet) {
            String queue = dto.getQueueType();
            LeagueEntry entry = existingByQueue.remove(queue);
            if (entry != null) {
                entry.update(dto.getLeagueId(), dto.getTier(), dto.getRank(), dto.getSummonerId(), dto.getLeaguePoints(), dto.getWins(), dto.getLosses(), dto.isHotStreak(), dto.isVeteran(), dto.isFreshBlood(), dto.isInactive());
            } else {
                entry = LeagueEntry.create(dto.getQueueType(), dto.getPuuid(), dto.getLeagueId(), dto.getTier(), dto.getRank(), dto.getSummonerId(), dto.getLeaguePoints(),
                        dto.getWins(), dto.getLosses(), dto.isHotStreak(), dto.isVeteran(), dto.isFreshBlood(), dto.isInactive());
            }
            toSave.add(entry);
        }

        List<LeagueEntry> toDelete = new ArrayList<>(existingByQueue.values());

        List<LeagueEntry> deleted = toDelete.stream().map(le -> {
            le.delete();
            return le;
        }).toList();
        List<LeagueEntry> updateLeagueEntrySet = leagueEntryRepository.saveAll(toSave);
        leagueEntryRepository.saveAll(deleted);

        HashSet<LeagueEntry> updated = new HashSet<>(updateLeagueEntrySet);

        String key = "league:entry:puuid:" + puuid;
        Set<LeagueEntryDto> updateSet = new HashSet<>();
        for (LeagueEntry leagueEntry : updated) {
            updateSet.add(leagueEntryMapper.toDto(leagueEntry));
        }
        leagueEntryCacheService.put(key, updateSet, LEAGUE_ENTRY_TTL);

        return updateSet;
    }

    @Transactional
    public LeagueListDto getChallengerLeague() {
        String key = "league:challenger";

        return leagueListCacheService.getOrLoad(
                key,
                () -> {
                    LeagueListDto dto = apiService.getLeagueList(LeagueQueue.RANKED_SOLO_5x5.getQueue())
                            .orElseThrow(LeagueListNotFoundException::new);

                    LeagueList entity = leagueListMapper.toEntity(dto);
                    LeagueList saved = leagueListRepository.save(entity);

                    return leagueListMapper.toDto(saved);
                    },
                LEAGUE_LIST_TTL
        );
    }

    @Scheduled(cron="0 0 0 * * ?")
    public void refreshChallengerLeague() {
        String key = "league:challenger";
        leagueListCacheService.evict(key);
        getChallengerLeague();
    }
}
