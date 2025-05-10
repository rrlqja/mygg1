package song.mygg1.domain.riot.dto;

import lombok.Data;
import song.mygg1.domain.riot.dto.account.AccountDto;
import song.mygg1.domain.riot.dto.league.LeagueEntryDto;
import song.mygg1.domain.riot.dto.match.MatchDto;
import song.mygg1.domain.riot.dto.summoner.SummonerDto;

import java.util.List;
import java.util.Set;

@Data
public class SearchDto {
    private AccountDto account;
    private SummonerDto summoner;
    private Set<LeagueEntryDto> leagueEntrySet;
    private List<MatchDto> matchList;

    public SearchDto(AccountDto account, SummonerDto summoner, Set<LeagueEntryDto> leagueEntrySet, List<MatchDto> matchList) {
        this.account = account;
        this.summoner = summoner;
        this.leagueEntrySet = leagueEntrySet;
        this.matchList = matchList;
    }
}
