package song.mygg1.domain.riot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import song.mygg1.domain.riot.dto.league.LeagueEntryDto;
import song.mygg1.domain.riot.entity.league.LeagueEntry;
import song.mygg1.domain.riot.repository.LeagueEntryJpaRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
}
