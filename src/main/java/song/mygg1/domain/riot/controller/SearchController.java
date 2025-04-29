package song.mygg1.domain.riot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import song.mygg1.domain.riot.dto.SearchDto;
import song.mygg1.domain.riot.service.SearchService;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/kr")
public class SearchController {
    private final SearchService searchService;
    private final ObjectMapper objectMapper;

    @GetMapping("/search")
    public String getSearch(@RequestParam(name = "query", required = false) String query,
                            Model model) throws JsonProcessingException {
        if (query.trim().isEmpty()) {
            return "redirect:/";
        }

        SearchDto search = searchService.search("vevee", "kr1", 0, 10);
        model.addAttribute("search", objectMapper.enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString(search));

        model.addAttribute("query", query);

        return "lol/search";
    }
}
