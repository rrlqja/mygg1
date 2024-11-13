package song.mygg.domain.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import song.mygg.domain.common.exception.post.PostNotFoundException;
import song.mygg.domain.post.dto.request.RequestSavePostDto;
import song.mygg.domain.post.dto.response.ResponseGetPostDto;
import song.mygg.domain.post.dto.response.ResponseSavePostDto;
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
    public ResponseSavePostDto savePost(RequestSavePostDto requestSavePostDto, UserDetailsImpl userDetails) {
        User writer = null;

        if (userDetails != null) {
            writer = userDetails.getUser();
        }

        Post post = Post.of(requestSavePostDto.getTitle(), requestSavePostDto.getContent(), writer, requestSavePostDto.getWriterName());

        Post savePost = postRepository.save(post);

        return new ResponseSavePostDto(savePost);
    }

    @Transactional
    public ResponseGetPostDto getPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        return new ResponseGetPostDto(post);
    }
}
