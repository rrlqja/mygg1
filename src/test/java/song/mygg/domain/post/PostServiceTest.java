package song.mygg.domain.post;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import song.mygg.domain.post.dto.request.RequestSavePostDto;
import song.mygg.domain.post.dto.response.ResponseGetPostDto;
import song.mygg.domain.post.dto.response.ResponseSavePostDto;
import song.mygg.domain.post.repository.PostJpaRepository;
import song.mygg.domain.post.service.PostService;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@TestPropertySource(properties = {
        "JASYPT_ENCRYPTOR_PASSWORD=song"
})
class PostServiceTest {
    @Autowired
    PostService postService;
    @Autowired
    PostJpaRepository postRepository;

    @Test
    void successSavePost() {
        RequestSavePostDto requestSavePostDto = new RequestSavePostDto();
        requestSavePostDto.setTitle("testTitle");
        requestSavePostDto.setContent("testContent");
        requestSavePostDto.setWriterName("testWriter");
        ResponseSavePostDto savePostDto = postService.savePost(requestSavePostDto, null);

        ResponseGetPostDto postDto = postService.getPostById(savePostDto.getId());

        assertThat(postDto.getWriterName())
                .isEqualTo(requestSavePostDto.getWriterName());

        assertThat(postRepository.findById(savePostDto.getId()).get().getWriterName())
                .isEqualTo(requestSavePostDto.getWriterName());
    }
}