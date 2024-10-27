package song.mygg.domain.loa.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import song.mygg.domain.loa.entity.Adventurer;
import song.mygg.domain.loa.service.AdventurerService;

@Slf4j
@Controller
@RequestMapping("/loa")
@RequiredArgsConstructor
public class LoaHomeController {
    private final AdventurerService adventurerService;

    @ResponseBody
    @GetMapping("/adventurer/{name}")
    public Adventurer getAdventurer(@PathVariable("name") String name) {

        return adventurerService.getAdventurer(name);
    }
}
