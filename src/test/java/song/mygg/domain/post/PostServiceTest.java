package song.mygg.domain.post;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import song.mygg.domain.post.dto.request.ReqSavePostDto;
import song.mygg.domain.post.dto.response.ResGetPostDto;
import song.mygg.domain.post.dto.response.ResSavePostDto;
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
        ReqSavePostDto reqSavePostDto = new ReqSavePostDto();
        reqSavePostDto.setTitle("testTitle");
        reqSavePostDto.setContent("testContent");
        reqSavePostDto.setWriterName("testWriter");
        ResSavePostDto savePostDto = postService.savePost(reqSavePostDto, null);

        ResGetPostDto postDto = postService.getPostById(savePostDto.getId());

        assertThat(postDto.getWriterName())
                .isEqualTo(reqSavePostDto.getWriterName());

        assertThat(postRepository.findById(savePostDto.getId()).get().getWriterName())
                .isEqualTo(reqSavePostDto.getWriterName());
    }
}