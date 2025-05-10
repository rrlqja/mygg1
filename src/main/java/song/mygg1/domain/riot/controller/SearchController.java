package song.mygg1.domain.riot.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;
import song.mygg1.domain.riot.dto.SearchDto;
import song.mygg1.domain.riot.service.SearchService;
import song.mygg1.domain.riot.service.recentsearch.RecentSearchService;

import java.nio.charset.StandardCharsets;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/kr")
public class SearchController {
    private final SearchService searchService;
    private final RecentSearchService recentSearchService;

    @GetMapping("/search")
    public String getSearch(@RequestParam(name = "query", required = false) String query,
                            HttpSession session,
                            Model model) {
        if (query == null || query.isBlank()) {
            return "redirect:/";
        }
        String[] queryParts = query.split("#", 2);
        String gameName = queryParts[0].trim();
        String tagLine = (queryParts.length > 1 && !queryParts[1].isBlank()) ? queryParts[1].trim() : "kr1";

        model.addAttribute("search", searchService.search(gameName, tagLine, 0, 10));
        model.addAttribute("query", query);

        recentSearchService.add(session.getId(), gameName, tagLine);
        model.addAttribute("recentSearch", recentSearchService.get(session.getId()));

        return "riot/search";
    }

    @GetMapping("/search/refresh")
    public String getRefresh(@RequestParam(name = "query", required = false) String query,
                             @RequestParam(name = "puuid") String puuid,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        SearchDto refresh = searchService.refresh(puuid);

        redirectAttributes.addFlashAttribute("recentSearch", recentSearchService.get(session.getId()));

        return "redirect:/kr/search?query=" + UriUtils.encode(query, StandardCharsets.UTF_8);
    }
}
