package song.mygg1.domain.riot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import song.mygg1.domain.riot.dto.SearchDto;
import song.mygg1.domain.riot.entity.account.Account;
import song.mygg1.domain.riot.entity.league.LeagueEntry;
import song.mygg1.domain.riot.entity.match.Matches;
import song.mygg1.domain.riot.entity.summoner.Summoner;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {
    private final AccountService accountService;
    private final SummonerService summonerService;
    private final LeagueService leagueService;
    private final MatchService matchService;

    /**
     * @param gameName
     * @param tagLine
     * 1. account
     * 2. summoner
     * 3. league
     * 4. match
     */
    @Transactional
    public SearchDto search(String gameName, String tagLine, Integer start, Integer count) {
        Account account = accountService.findAccountByGameNameAndTagLine(gameName, tagLine);
        Summoner summoner = summonerService.getSummoner(account.getPuuid());
        Set<LeagueEntry> leagueEntrySet = leagueService.getLeague(account.getPuuid());
        List<Matches> matchesList = matchService.getMatchList(account.getPuuid(), start, count);

        return new SearchDto(account, summoner, leagueEntrySet, matchesList);
    }
}
