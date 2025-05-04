package song.mygg1.domain.riot.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import song.mygg1.domain.riot.dto.match.MatchDto;
import song.mygg1.domain.riot.entity.QueueType;
import song.mygg1.domain.riot.service.MatchService;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/match")
@RequiredArgsConstructor
public class MatchController {
    private final MatchService matchService;

    @GetMapping("/{matchId}")
    public String getMatchDetail(@PathVariable(name = "matchId") String matchId,
                                 @RequestParam(name = "puuid") String puuid,
                                 Model model) {
        MatchDto matchDetail = matchService.getMatchDetail(matchId, puuid);

        model.addAttribute("matchDetail", matchDetail);

        if (QueueType.ARENA.getDisplayName().equals(matchDetail.getInfo().getQueueType())) {
            return "common/fragments::matchDetailArena(match=${matchDetail})";
        } else {
            return "common/fragments::matchDetail(match=${matchDetail})";
        }
    }

    @GetMapping("/list")
    public String getMatchList(@RequestParam(name = "puuid") String puuid,
                               @RequestParam(name = "start") Integer start,
                               @RequestParam(name = "count") Integer count,
                               Model model) {
        List<MatchDto> matches = matchService.getMoreMatchList(puuid, start, count);

        model.addAttribute("matches", matches);
        model.addAttribute("puuid", puuid);

        return "common/fragments::matchItems(matches = ${matches}, puuid = ${puuid})";
    }
}
