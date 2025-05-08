package song.mygg1.domain.common.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import song.mygg1.domain.redis.service.RedisService;
import song.mygg1.domain.riot.dto.league.LeagueItemSummonerDto;
import song.mygg1.domain.riot.service.league.LeagueItemService;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {
    private final RedisService redisService;
    private final LeagueItemService leagueItemService;

    @GetMapping("/")
    public String getHome(HttpSession session,
                          Model model) {
        model.addAttribute("recentSearch", redisService.getRecentSearch(session.getId()));
        model.addAttribute("freeChampion", redisService.getFreeChampion());
        model.addAttribute("freeChampionForN", redisService.getFreeChampionForNewP());

        List<LeagueItemSummonerDto> leagueItemList = leagueItemService.getLeagueItemList(
                redisService.getChallengerLeagueBySummonerId()
        );
        model.addAttribute("leagueItemList", leagueItemList);

        return "common/home";
    }
}
