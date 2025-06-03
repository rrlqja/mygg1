package song.mygg1.domain.riot.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import song.mygg1.domain.riot.dto.champion.ChampionBanPickDto;
import song.mygg1.domain.riot.service.champion.ChampionBuildService;
import song.mygg1.domain.riot.service.champion.ChampionService;
import song.mygg1.domain.riot.service.champion.ChampionStatsService;
import song.mygg1.domain.riot.service.league.LeagueItemService;
import song.mygg1.domain.riot.service.league.LeagueListService;
import song.mygg1.domain.riot.service.recentsearch.RecentSearchService;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/champion")
public class ChampionController {
    private final ChampionService championService;
    private final RecentSearchService recentSearchService;
    private final LeagueItemService leagueItemService;
    private final ChampionBuildService championBuildService;
    private final ChampionStatsService championStatsService;

    @GetMapping("/stats")
    public String getStats(@PageableDefault(size = 10, page = 0) Pageable pageable,
                           HttpSession session,
                           Model model) {
        model.addAttribute("leagueItemList", leagueItemService.getRankLeagueItemList(LeagueListService.CHALLENGER_LEAGUE_ID, pageable));
        model.addAttribute("recentSearch", recentSearchService.get(session.getId()));

        List<ChampionBanPickDto> championStats = championStatsService.getAllChampionStats();
        model.addAttribute("championStats", championStats);

        return "riot/championStats";
    }

    @GetMapping("/{championId}")
    public String getChampion(@PathVariable("championId") Long championId,
                              HttpSession session,
                              Model model) {
        model.addAttribute("championInfo", championService.getChampionInfo(championId));
        model.addAttribute("championMasteryRanking", championService.getChampionMasteryRanking(championId));

        model.addAttribute("leagueItemList", leagueItemService.getRankLeagueItemList(LeagueListService.CHALLENGER_LEAGUE_ID, PageRequest.of(0, 10)));
        model.addAttribute("recentSearch", recentSearchService.get(session.getId()));

        model.addAttribute("championItemBuild", championBuildService.getChampionItemBuild(championId.intValue()));
        model.addAttribute("championSkillTree", championBuildService.getChampionSkillTree(championId.intValue()));

        return "riot/champion";
    }
}
