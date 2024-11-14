package song.mygg.domain.post.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import song.mygg.domain.post.dto.request.RequestSavePostDto;
import song.mygg.domain.post.dto.response.ResponseGetPostDto;
import song.mygg.domain.post.dto.response.ResponseSavePostDto;
import song.mygg.domain.post.service.PostService;
import song.mygg.security.authentication.userdetails.UserDetailsImpl;

@Slf4j
@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/{postId}")
    public ResponseEntity<ResponseGetPostDto> getPostByPostId(@PathVariable("postId") Long postId) {
        ResponseGetPostDto responsePostDto = postService.getPostById(postId);

        return ResponseEntity.ok(responsePostDto);
    }

    @ResponseBody
    @PostMapping("/save")
    public ResponseEntity<ResponseSavePostDto> postSavePost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                            @ModelAttribute RequestSavePostDto requestSavePostDto) {
        ResponseSavePostDto responseSavePostDto = postService.savePost(requestSavePostDto, userDetails);

        return ResponseEntity.ok(responseSavePostDto);
    }
}
