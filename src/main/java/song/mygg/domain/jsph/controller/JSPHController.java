package song.mygg.domain.jsph.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import song.mygg.domain.jsph.entity.JSPHPost;
import song.mygg.domain.jsph.service.JSPHService;

@Slf4j
@Controller
@RequestMapping("/jsph")
@RequiredArgsConstructor
public class JSPHController {
    private final JSPHService jsphService;

    @ResponseBody
    @GetMapping("/{id}")
    public ResponseEntity<JSPHPost> getJSPHPostById(@PathVariable("id") Long id) {
        JSPHPost jsphPost = jsphService.getJSPHPostById(id);
        return ResponseEntity.ok(jsphPost);
    }
}
