package song.mygg.domain.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import song.mygg.domain.common.exception.post.PostNotFoundException;
import song.mygg.domain.post.dto.request.ReqSavePostDto;
import song.mygg.domain.post.dto.response.ResGetPostDto;
import song.mygg.domain.post.dto.response.ResSavePostDto;
import song.mygg.domain.post.entity.Post;
import song.mygg.domain.post.repository.PostJpaRepository;
import song.mygg.domain.user.entity.User;
import song.mygg.security.authentication.userdetails.UserDetailsImpl;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostJpaRepository postRepository;

    @Transactional
    public ResSavePostDto savePost(ReqSavePostDto reqSavePostDto, UserDetailsImpl userDetails) {
        User writer = null;

        if (userDetails != null) {
            writer = userDetails.getUser();
        }

        Post post = Post.of(reqSavePostDto.getTitle(), reqSavePostDto.getContent(), writer, reqSavePostDto.getWriterName());

        Post savePost = postRepository.save(post);

        return new ResSavePostDto(savePost);
    }

    @Transactional
    public ResGetPostDto getPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        return new ResGetPostDto(post);
    }
}
