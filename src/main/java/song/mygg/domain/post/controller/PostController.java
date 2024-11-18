package song.mygg.domain.post.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import song.mygg.domain.post.dto.request.ReqSavePostDto;
import song.mygg.domain.post.dto.response.ResGetPostDto;
import song.mygg.domain.post.dto.response.ResSavePostDto;
import song.mygg.domain.post.service.PostService;
import song.mygg.security.authentication.userdetails.UserDetailsImpl;

@Slf4j
@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/{postId}")
    public String getPostByPostId(@PathVariable("postId") Long postId,
                                  Model model) {
        ResGetPostDto responsePostDto = postService.getPostById(postId);

        model.addAttribute("post", responsePostDto);

        return "post/post";
    }

    @PostMapping("/save")
    public String postSavePost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                               @ModelAttribute ReqSavePostDto reqSavePostDto) {
        ResSavePostDto resSavePostDto = postService.savePost(reqSavePostDto, userDetails);

        return "redirect:/post/" + resSavePostDto.getId();
    }

}
