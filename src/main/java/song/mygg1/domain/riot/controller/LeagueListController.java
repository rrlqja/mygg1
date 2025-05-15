package song.mygg1.domain.riot.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import song.mygg1.domain.riot.service.league.LeagueListService;

@Slf4j
@Controller
@RequestMapping("/leagueList")
@RequiredArgsConstructor
public class LeagueListController {
    private final LeagueListService leagueListService;

    @GetMapping
    public String getLeagueList() {

        return "riot/leagueList";
    }
}
