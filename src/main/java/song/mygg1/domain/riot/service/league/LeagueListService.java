package song.mygg1.domain.riot.service.league;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import song.mygg1.domain.riot.repository.league.LeagueListJpaRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class LeagueListService {
    private final LeagueListJpaRepository leagueListRepository;


}
