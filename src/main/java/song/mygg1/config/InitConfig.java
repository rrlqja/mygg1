package song.mygg1.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import song.mygg1.domain.redis.service.RedisService;
import song.mygg1.domain.riot.entity.account.Account;
import song.mygg1.domain.riot.entity.league.LeagueEntry;
import song.mygg1.domain.riot.entity.league.LeagueItem;
import song.mygg1.domain.riot.entity.match.Matches;
import song.mygg1.domain.riot.entity.summoner.Summoner;
import song.mygg1.domain.riot.repository.league.LeagueItemJpaRepository;
import song.mygg1.domain.riot.service.account.AccountService;
import song.mygg1.domain.riot.service.champion.ChampionService;
import song.mygg1.domain.riot.service.league.LeagueItemService;
import song.mygg1.domain.riot.service.league.LeagueService;
import song.mygg1.domain.riot.service.match.MatchService;
import song.mygg1.domain.riot.service.summoner.SummonerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@Profile("!dev")
@RequiredArgsConstructor
public class InitConfig {
    private final InitService initService;

    @PostConstruct
    public void setInit() {
        initService.init();
    }

    @Component
    @RequiredArgsConstructor
    private static class InitService {
        private final ChampionService championService;
        private final RedisService redisService;
        private final LeagueItemJpaRepository leagueItemRepository;
        private final AccountService accountService;
        private final SummonerService summonerService;
        private final LeagueService leagueService;
        private final MatchService matchService;

        public void init() {
            championService.setChampionList();

            redisService.setChampionRotations();
            redisService.setChallengerLeague();

//            Page<LeagueItem> leagueItemList = leagueItemRepository.findAll(PageRequest.of(0, 100));
//            List<Account> accountList = accountService.findAccountList(leagueItemList.stream().map(LeagueItem::getPuuid).toList());
//            List<Summoner> summonerList = summonerService.getSummonerList(leagueItemList.stream().map(LeagueItem::getSummonerId).toList());
//
//            for (Summoner summoner : summonerList) {
//                Set<LeagueEntry> leagueEntrySet = leagueService.getLeague(summoner.getPuuid());
//            }
//
//            for (Account account : accountList) {
//                List<Matches> matchesList = matchService.getMatchList(account.getPuuid(), 0, 100);
//            }

        }
    }
}
