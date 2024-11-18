package song.mygg.domain.post.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import song.mygg.domain.post.dto.request.ReqSavePostDto;
import song.mygg.domain.post.dto.response.ResGetPostDto;
import song.mygg.domain.post.dto.response.ResSavePostDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest
@TestPropertySource(properties = {
        "JASYPT_ENCRYPTOR_PASSWORD=song"
})
@AutoConfigureMockMvc
class PostControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @ParameterizedTest
    @ValueSource(longs = {-1L, -10L})
    void notFoundPostByPostId(Long postId) throws Exception {
        mockMvc.perform(get("/post/{postId}", postId))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails("1")
    void successSavePost() throws Exception {
        ReqSavePostDto reqSavePostDto = new ReqSavePostDto();
        reqSavePostDto.setTitle("test title");
        reqSavePostDto.setContent("test content");
        reqSavePostDto.setWriterName("test writer");

        MvcResult mvcResult = mockMvc.perform(post("/post/save")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("title", reqSavePostDto.getTitle())
                        .param("content", reqSavePostDto.getContent())
                        .param("writerName", reqSavePostDto.getWriterName()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/post/*"))
                .andReturn();

        String redirectedUrl = mvcResult.getResponse().getRedirectedUrl();

        mockMvc.perform(get(redirectedUrl))
                .andExpect(status().isOk())
                .andExpect(view().name("post/post"))
                .andExpect(model().attributeExists("post"));
    }

}