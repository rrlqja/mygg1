package song.mygg.domain.common.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CommonController {

    @RequestMapping(value = "/", method = {POST, GET})
    public String getHome(Model model) {
        model.addAttribute("posts", List.of("t1", "t2", "t3", "t4"));

        return "home";
    }

    @RequestMapping(value = "/login", method = {POST, GET})
    public String getLogin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() &&
                !(authentication.getPrincipal() instanceof String && authentication.getPrincipal().equals("anonymousUser"))) {
            return "redirect:/";
        }
        return "login";
    }
}

