package song.mygg1.domain.riot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import song.mygg1.domain.riot.dto.league.LeagueEntryDto;
import song.mygg1.domain.riot.entity.league.LeagueEntry;
import song.mygg1.domain.riot.repository.LeagueEntryJpaRepository;

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
    private final LeagueEntryJpaRepository leagueEntryRepository;
    private final ApiService apiService;

    @Transactional
    public Set<LeagueEntry> getLeague(String puuid) {
        Set<LeagueEntry> leagueEntrySet = leagueEntryRepository.findLeagueEntriesById_Puuid(puuid);

        if (leagueEntrySet.isEmpty()) {
            Set<LeagueEntryDto> apiLeagueEntryDtoSet = apiService.getLeagueEntry(puuid);

            List<LeagueEntry> apiLeagueEntrySet = apiLeagueEntryDtoSet.stream()
                    .map(LeagueEntryDto::toEntity).toList();

            return new HashSet<>(leagueEntryRepository.saveAll(apiLeagueEntrySet));
        }

        return leagueEntrySet;
    }

    @Transactional
    public Set<LeagueEntry> refreshLeague(String puuid) {
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

        return new HashSet<>(updateLeagueEntrySet);
    }
}
