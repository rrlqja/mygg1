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
@Profile("dev")
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
            LeagueListDto challenger = leagueService.getChallengerLeague();
            LeagueListDto grandmaster = leagueService.getGrandmasterLeague();
            LeagueListDto master = leagueService.getMasterLeague();
            leagues.add(challenger);
            leagues.add(grandmaster);
            leagues.add(master);

            for (LeagueListDto league : leagues) {
                log.info("[leage: {}] get leagueItem", league.getTier());
                List<LeagueItemSummonerDto> leagueItemList = leagueItemService.getLeagueItemList(league, 9999);

                log.info("[leage: {}] get account, summoner", league.getTier());
                List<AccountDto> accountList = new ArrayList<>();
                List<SummonerDto> summonerList = new ArrayList<>();
                for (LeagueItemSummonerDto dto : leagueItemList) {
                    log.info("[leage: {}] get account by leagueItemSummonerDto [gameName: {}#{}, total: {}, current: {}]", league.getTier(), dto.getGameName(), dto.getTagLine(), leagueItemList.size(), accountList.size());
                    accountList.add(accountService.findAccountByGameNameAndTagLine(dto.getGameName(), dto.getTagLine()));

                    log.info("[leage: {}] get summoner by leagueItemSummonerDto [puuid: {}]", league.getTier(), dto.getPuuid());
                    summonerList.add(summonerService.getSummoner(dto.getPuuid()));
                }

                log.info("[leage: {}] get leagueEntry", league.getTier());
                List<Set<LeagueEntryDto>> leagueEntrySetList = new ArrayList<>();
                for (SummonerDto dto : summonerList) {
                    log.info("[leage: {}] get leagueEntry by summonerDto [puuid: {}]", league.getTier(), dto.getPuuid());
                    leagueEntrySetList.add(leagueService.refreshLeague(dto.getPuuid()));
                    log.info("[leage: {}] leagueEntrySet total: {} current: {}", league.getTier(), summonerList.size(), leagueEntrySetList.size());
                }

                log.info("[leage: {}] get match", league.getTier());
                List<List<MatchDto>> matchListList = new ArrayList<>();
                for (AccountDto account : accountList) {
                    matchListList.add(matchService.refreshMatchList(account.getPuuid(), 0, 30));
                    log.info("[leage: {}] get match by accountDto [puuid: {}, total: {}, current: {}]", league.getTier(), account.getPuuid(), accountList.size(), matchListList.size());
                }
            }
        }
    }
}
