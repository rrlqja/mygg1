package song.mygg1.domain.riot.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import song.mygg1.domain.riot.dto.league.LeagueItemSummonerDto;
import song.mygg1.domain.riot.service.league.LeagueItemService;
import song.mygg1.domain.riot.service.league.LeagueListService;
import song.mygg1.domain.riot.service.recentsearch.RecentSearchService;

@Slf4j
@Controller
@RequestMapping("/leagueList")
@RequiredArgsConstructor
public class LeagueListController {
    private final LeagueItemService leagueItemService;
    private final RecentSearchService recentSearchService;

    @GetMapping("/kr")
    public String getLeagueList(@PageableDefault(size = 50, page = 0) Pageable pageable,
                                HttpSession session,
                                Model model) {
        model.addAttribute("recentSearch", recentSearchService.get(session.getId()));

        Page<LeagueItemSummonerDto> leagueList = leagueItemService.getRankLeagueItemList(LeagueListService.CHALLENGER_LEAGUE_ID, pageable);
        model.addAttribute("leagueList", leagueList);

        int currentPage = leagueList.getNumber();
        int totalPages = leagueList.getTotalPages();

        int pageGroupSize = 10;
        int startPage = Math.max(0, currentPage / pageGroupSize * pageGroupSize);
        int endPage = Math.min(totalPages - 1, startPage + pageGroupSize - 1);

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("pageGroupSize", pageGroupSize);

        return "riot/leagueList";
    }
}
