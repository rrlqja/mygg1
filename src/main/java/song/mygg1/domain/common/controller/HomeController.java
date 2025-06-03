package song.mygg1.domain.common.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import song.mygg1.domain.riot.service.champion.ChampionRotationService;
import song.mygg1.domain.riot.service.champion.ChampionService;
import song.mygg1.domain.riot.service.league.LeagueItemService;
import song.mygg1.domain.riot.service.league.LeagueListService;
import song.mygg1.domain.riot.service.match.ParticipantService;
import song.mygg1.domain.riot.service.recentsearch.RecentSearchService;

@Slf4j
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {
    private final RecentSearchService recentSearchService;
    private final ChampionService championService;
    private final ChampionRotationService championRotationService;
    private final LeagueItemService leagueItemService;
    private final ParticipantService participantService;

    @GetMapping("/")
    public String getHome(@PageableDefault(size = 10, page = 0) Pageable pageable,
                          HttpSession session,
                          Model model) {
        model.addAttribute("recentSearch", recentSearchService.get(session.getId()));

        model.addAttribute("freeChampion", championService.getChampion(championRotationService.getFreeChampionIds()));
        model.addAttribute("freeChampionForN", championService.getChampion(championRotationService.getFreeChampionIdsForNewPlayers()));
        model.addAttribute("leagueItemList", leagueItemService.getRankLeagueItemList(LeagueListService.CHALLENGER_LEAGUE_ID, pageable));
//        model.addAttribute("leagueItemList", leagueItemService.getLeagueItemList(leagueService.getGrandmasterLeague()));

        model.addAttribute("dailyWinRateList", participantService.getWinRateDaily());
        model.addAttribute("weeklyWinRateList", participantService.getWinRateWeekly());

        return "common/home";
    }
}
