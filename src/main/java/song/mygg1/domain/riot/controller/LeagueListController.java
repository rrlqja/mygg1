package song.mygg1.domain.riot.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import song.mygg1.domain.riot.service.league.LeagueListService;
import song.mygg1.domain.riot.service.recentsearch.RecentSearchService;

@Slf4j
@Controller
@RequestMapping("/leagueList")
@RequiredArgsConstructor
public class LeagueListController {
    private final LeagueListService leagueListService;
    private final RecentSearchService recentSearchService;

    @GetMapping("/kr")
    public String getLeagueList(@PageableDefault(size = 10, page = 0) Pageable pageable,
                                HttpSession session,
                                Model model) {
        model.addAttribute("recentSearch", recentSearchService.get(session.getId()));

//        leagueListService.getLeagueRanking(pageable);

        return "riot/leagueList";
    }
}
