package song.mygg1.domain.riot.service.league;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import song.mygg1.domain.redis.service.CacheService;
import song.mygg1.domain.riot.dto.league.LeagueItemDto;
import song.mygg1.domain.riot.dto.league.LeagueItemSummonerDto;
import song.mygg1.domain.riot.dto.league.LeagueListDto;
import song.mygg1.domain.riot.entity.account.Account;
import song.mygg1.domain.riot.entity.league.LeagueItem;
import song.mygg1.domain.riot.entity.champion.ChampionMastery;
import song.mygg1.domain.riot.entity.summoner.Summoner;
import song.mygg1.domain.riot.repository.league.LeagueItemJpaRepository;
import song.mygg1.domain.riot.service.account.AccountService;
import song.mygg1.domain.riot.service.champion.ChampionMasteryService;
import song.mygg1.domain.riot.service.summoner.SummonerService;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LeagueItemService {
    private final CacheService<List<LeagueItemSummonerDto>> cacheService;
    private final LeagueItemJpaRepository leagueItemRepository;
    private final SummonerService summonerService;
    private final AccountService accountService;
    private final ChampionMasteryService championMasteryService;

    private static final Duration TTL = Duration.ofMinutes(30);

    @Transactional(readOnly = true)
    public List<LeagueItemSummonerDto> getLeagueItemList(LeagueListDto leagueDto) {
        String key = "league:itemList:" + leagueDto.getQueue() + ":" + leagueDto.getTier();

        return cacheService.getOrLoad(key, () -> {
            List<String> summonerIdList = leagueDto.getEntries().stream()
                    .sorted(Comparator.comparingInt(LeagueItemDto::getLeaguePoints).reversed())
                    .limit(10)
                    .map(LeagueItemDto::getSummonerId)
                    .toList();

            List<LeagueItem> leagueItemList =
                    leagueItemRepository.findLeagueItemsBySummonerIdIn(summonerIdList);

            List<Summoner> summonerList =
                    summonerService.getSummonerList(
                            leagueItemList.stream().map(LeagueItem::getSummonerId).toList()
                    );
            List<Account> accountList =
                    accountService.findAccountList(
                            summonerList.stream().map(Summoner::getPuuid).toList()
                    );

            Map<String, Summoner> summonerMap = summonerList.stream()
                    .collect(Collectors.toMap(Summoner::getId, Function.identity()));
            Map<String, Account>   accountMap   = accountList.stream()
                    .collect(Collectors.toMap(Account::getPuuid, Function.identity()));

            return leagueItemList.stream()
                    .map(li -> {
                        Summoner s = summonerMap.get(li.getSummonerId());
                        Account  a = (s != null ? accountMap.get(s.getPuuid()) : null);
                        List<ChampionMastery> cm = (a != null
                                ? championMasteryService.getChampionMastery(a.getPuuid(), 3)
                                : List.of());
                        return new LeagueItemSummonerDto(li, s, a, cm);
                    })
                    .sorted(Comparator.comparingInt(LeagueItemSummonerDto::getLeaguePoints).reversed())
                    .toList();
        }, TTL);
    }
}
