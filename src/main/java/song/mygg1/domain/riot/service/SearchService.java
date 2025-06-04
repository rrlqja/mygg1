package song.mygg1.domain.riot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import song.mygg1.domain.riot.dto.SearchDto;
import song.mygg1.domain.riot.dto.account.AccountDto;
import song.mygg1.domain.riot.dto.league.LeagueEntryDto;
import song.mygg1.domain.riot.dto.match.MatchDto;
import song.mygg1.domain.riot.dto.summoner.SummonerDto;
import song.mygg1.domain.riot.mapper.SearchMapper;
import song.mygg1.domain.riot.service.account.AccountService;
import song.mygg1.domain.riot.service.league.LeagueService;
import song.mygg1.domain.riot.service.match.MatchService;
import song.mygg1.domain.riot.service.summoner.SummonerService;

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
    private final SearchMapper searchMapper;

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
        AccountDto account = accountService.findAccountByGameNameAndTagLine(gameName, tagLine);
        SummonerDto summoner = summonerService.getSummoner(account.getPuuid());
        Set<LeagueEntryDto> leagueEntrySet = leagueService.getLeague(account.getPuuid());
        List<MatchDto> matchesList = matchService.getMatchList(account.getPuuid(), start, count);

        return searchMapper.toDto(account, summoner, leagueEntrySet, matchesList);
    }

    @Transactional
    public SearchDto refresh(String puuid) {
        AccountDto account = accountService.findAccountByPuuidAndUpdate(puuid);
        SummonerDto summoner = summonerService.refreshSummoner(account.getPuuid());
        Set<LeagueEntryDto> updateLeagueEntrySet = leagueService.refreshLeague(account.getPuuid());
        List<MatchDto> updateMatchesList = matchService.refreshMatchList(account.getPuuid(), 0, 100);

        return searchMapper.toDto(account, summoner, updateLeagueEntrySet, updateMatchesList);
    }
}
