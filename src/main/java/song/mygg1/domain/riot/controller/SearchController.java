package song.mygg1.domain.riot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import song.mygg1.domain.redis.service.RedisService;
import song.mygg1.domain.riot.dto.SearchDto;
import song.mygg1.domain.riot.service.SearchService;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/kr")
public class SearchController {
    private final SearchService searchService;
    private final RedisService redisService;
    private final ObjectMapper objectMapper;

    @GetMapping("/search")
    public String getSearch(@RequestParam(name = "query", required = false) String query,
                            HttpSession session,
                            Model model) throws JsonProcessingException {
        if (query == null || query.isBlank()) {
            return "redirect:/";
        }
        String[] queryParts = query.split("#", 2);
        String gameName = queryParts[0].trim();
        String tagLine = (queryParts.length > 1 && !queryParts[1].isBlank()) ? queryParts[1].trim() : "kr1";

        SearchDto search = searchService.search(gameName, tagLine, 0, 10);

        model.addAttribute("query", query);
        model.addAttribute("search", search);
        model.addAttribute("searchJson", objectMapper.enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString(search));

        redisService.addRecentSearch(session.getId(), gameName, tagLine);
        model.addAttribute("recentSearch",
                redisService.getRecentSearch(session.getId())
        );

        return "riot/search";
    }
}
