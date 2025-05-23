package song.mygg1.domain.riot.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import song.mygg1.domain.riot.service.champion.ChampionBuildService;
import song.mygg1.domain.riot.service.champion.ChampionService;
import song.mygg1.domain.riot.service.league.LeagueItemService;
import song.mygg1.domain.riot.service.league.LeagueService;
import song.mygg1.domain.riot.service.recentsearch.RecentSearchService;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/champion")
public class ChampionController {
    private final ChampionService championService;
    private final RecentSearchService recentSearchService;
    private final LeagueService leagueService;
    private final LeagueItemService leagueItemService;
    private final ChampionBuildService championBuildService;

    @GetMapping("/{championId}")
    public String getChampion(@PathVariable("championId") Long championId,
                              HttpSession session,
                              Model model) {

        model.addAttribute("championInfo", championService.getChampionInfo(championId));
        model.addAttribute("championMasteryRanking", championService.getChampionMasteryRanking(championId));

        model.addAttribute("leagueItemList", leagueItemService.getLeagueItemList(leagueService.getChallengerLeague()));
        model.addAttribute("recentSearch", recentSearchService.get(session.getId()));

        model.addAttribute("championItemBuild", championBuildService.getChampionItemBuild(championId.intValue()));

        return "riot/champion";
    }
}
