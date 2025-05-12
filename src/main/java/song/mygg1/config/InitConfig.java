package song.mygg1.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import song.mygg1.domain.riot.dto.account.AccountDto;
import song.mygg1.domain.riot.dto.league.LeagueEntryDto;
import song.mygg1.domain.riot.dto.league.LeagueItemSummonerDto;
import song.mygg1.domain.riot.dto.league.LeagueListDto;
import song.mygg1.domain.riot.dto.match.MatchDto;
import song.mygg1.domain.riot.dto.summoner.SummonerDto;
import song.mygg1.domain.riot.entity.account.Account;
import song.mygg1.domain.riot.entity.summoner.Summoner;
import song.mygg1.domain.riot.service.account.AccountService;
import song.mygg1.domain.riot.service.league.LeagueItemService;
import song.mygg1.domain.riot.service.league.LeagueService;
import song.mygg1.domain.riot.service.match.MatchService;
import song.mygg1.domain.riot.service.summoner.SummonerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
        private final LeagueItemService leagueItemService;
        private final AccountService accountService;
        private final SummonerService summonerService;
        private final LeagueService leagueService;
        private final MatchService matchService;

//        @Transactional
        public void init() {
            log.info("get league");
            List<LeagueListDto> leagues = new ArrayList<>();
//            leagues.add(leagueService.getChallengerLeague());
//            leagues.add(leagueService.getGrandmasterLeague());
//            leagues.add(leagueService.getMasterLeague());

            for (LeagueListDto league : leagues) {
                log.info("get leagueItem");
                List<LeagueItemSummonerDto> leagueItemList = leagueItemService.getLeagueItemList(league, 9999);

                log.info("get account, summoner");
                List<AccountDto> accountList = new ArrayList<>();
                List<SummonerDto> summonerList = new ArrayList<>();
                for (LeagueItemSummonerDto dto : leagueItemList) {
                    log.info("get account by leagueItemSummonerDto [gameName: {}#{}, total: {}, current: {}]", dto.getGameName(), dto.getTagLine(), leagueItemList.size(), accountList.size());
                    accountList.add(accountService.findAccountByGameNameAndTagLine(dto.getGameName(), dto.getTagLine()));

                    log.info("get summoner by leagueItemSummonerDto [puuid: {}]", dto.getPuuid());
                    summonerList.add(summonerService.getSummoner(dto.getPuuid()));
                }

                log.info("get leagueEntry");
                List<Set<LeagueEntryDto>> leagueEntrySetList = new ArrayList<>();
                for (SummonerDto dto : summonerList) {
                    log.info("get leagueEntry by summonerDto [puuid: {}]", dto.getPuuid());
                    leagueEntrySetList.add(leagueService.refreshLeague(dto.getPuuid()));
                    log.info("leagueEntrySet total: {} current: {}", summonerList.size(), leagueEntrySetList.size());
                }

                log.info("get match");
                List<List<MatchDto>> matchListList = new ArrayList<>();
                for (AccountDto account : accountList) {
                    matchListList.add(matchService.refreshMatchList(account.getPuuid(), 0, 30));
                    log.info("get match by accountDto [puuid: {}, total: {}, current: {}]", account.getPuuid(), accountList.size(), matchListList.size());
                }
            }

//            log.info("get leagueItem");
//            List<LeagueItemSummonerDto> leagueItemList = leagueItemService.getLeagueItemList(league, 9999);
//
//            log.info("get account, summoner");
//            List<AccountDto> accountList = new ArrayList<>();
//            List<SummonerDto> summonerList = new ArrayList<>();
//            for (LeagueItemSummonerDto dto : leagueItemList) {
//                log.info("get account by leagueItemSummonerDto [gameName: {}#{}, total: {}, current: {}]", dto.getGameName(), dto.getTagLine(), leagueItemList.size(), accountList.size());
//                accountList.add(accountService.findAccountByGameNameAndTagLine(dto.getGameName(), dto.getTagLine()));
//
//                log.info("get summoner by leagueItemSummonerDto [puuid: {}]", dto.getPuuid());
//                summonerList.add(summonerService.getSummoner(dto.getPuuid()));
//            }
//
//            log.info("get leagueEntry");
//            List<Set<LeagueEntryDto>> leagueEntrySetList = new ArrayList<>();
//            for (SummonerDto dto : summonerList) {
//                log.info("get leagueEntry by summonerDto [puuid: {}]", dto.getPuuid());
//                leagueEntrySetList.add(leagueService.refreshLeague(dto.getPuuid()));
//                log.info("leagueEntrySet total: {} current: {}", summonerList.size(), leagueEntrySetList.size());
//            }
//
//            log.info("get match");
//            List<List<MatchDto>> matchListList = new ArrayList<>();
//            for (AccountDto account : accountList) {
//                matchListList.add(matchService.refreshMatchList(account.getPuuid(), 0, 30));
//                log.info("get match by accountDto [puuid: {}, total: {}, current: {}]", account.getPuuid(), accountList.size(), matchListList.size());
//            }
        }
    }
}
