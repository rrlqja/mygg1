package song.mygg1.domain.riot.dto;

import lombok.Data;
import song.mygg1.domain.riot.dto.account.AccountDto;
import song.mygg1.domain.riot.dto.league.LeagueEntryDto;
import song.mygg1.domain.riot.dto.match.MatchDto;
import song.mygg1.domain.riot.dto.summoner.SummonerDto;
import song.mygg1.domain.riot.entity.account.Account;
import song.mygg1.domain.riot.entity.league.LeagueEntry;
import song.mygg1.domain.riot.entity.match.Matches;
import song.mygg1.domain.riot.entity.summoner.Summoner;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class SearchDto {
    private AccountDto account;
    private SummonerDto summoner;
    private Set<LeagueEntryDto> leagueEntrySet;
    private List<MatchDto> matchList;

    public SearchDto(Account account, Summoner summoner, Set<LeagueEntry> leagueEntrySet, List<Matches> matchesList) {
        this.account = new AccountDto(account);
        this.summoner = new SummonerDto(summoner);
        this.leagueEntrySet = leagueEntrySet.stream().map(LeagueEntryDto::new).collect(Collectors.toSet());
        this.matchList = matchesList.stream().map(m -> new MatchDto(m, account.getPuuid())).toList();
    }
}
