package song.mygg.domain.common.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CommonController {

    @GetMapping("/")
    public String getHome(Model model) {
        model.addAttribute("name", null);

        return "home";
    }

    @PostMapping("/login")
    public String getLogin() {

        return "login";
    }
}

