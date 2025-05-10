package song.mygg1.domain.riot.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import song.mygg1.domain.riot.dto.SearchDto;
import song.mygg1.domain.riot.dto.account.AccountDto;
import song.mygg1.domain.riot.dto.league.LeagueEntryDto;
import song.mygg1.domain.riot.dto.match.MatchDto;
import song.mygg1.domain.riot.dto.summoner.SummonerDto;
import song.mygg1.domain.riot.entity.account.Account;
import song.mygg1.domain.riot.entity.league.LeagueEntry;
import song.mygg1.domain.riot.entity.match.Matches;
import song.mygg1.domain.riot.entity.summoner.Summoner;
import song.mygg1.domain.riot.mapper.account.AccountMapper;
import song.mygg1.domain.riot.mapper.league.LeagueEntryMapper;
import song.mygg1.domain.riot.mapper.match.MatchMapper;
import song.mygg1.domain.riot.mapper.summoner.SummonerMapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SearchMapper {
    private final AccountMapper accountMapper;
    private final SummonerMapper summonerMapper;
    private final LeagueEntryMapper leagueEntryMapper;
    private final MatchMapper matchMapper;

    public SearchDto toDto(AccountDto account, SummonerDto summoner, Set<LeagueEntryDto> leagueEntrySet, List<MatchDto> matchesSet) {
//        AccountDto accountDto = accountMapper.toDto(account);

//        SummonerDto summonerDto = summonerMapper.toDto(summoner);

//        Set<LeagueEntryDto> leagueEntryDtoSet = leagueEntrySet.stream()
//                .map(leagueEntryMapper::toDto)
//                .collect(Collectors.toSet());

//        List<MatchDto> matchDtoList = matchesSet.stream()
//                .map(m -> {
//                    return matchMapper.toDto(m, account.getPuuid());
//                })
//                .toList();

        return new SearchDto(account, summoner, leagueEntrySet, matchesSet);
    }
}
