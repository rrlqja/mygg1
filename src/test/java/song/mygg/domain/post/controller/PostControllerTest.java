package song.mygg.domain.post.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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
import song.mygg.domain.post.dto.request.RequestSavePostDto;

import java.util.Map;

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
        RequestSavePostDto requestSavePostDto = new RequestSavePostDto();
        requestSavePostDto.setTitle("test title");
        requestSavePostDto.setContent("test content");
        requestSavePostDto.setWriterName("test writer");

        MvcResult mvcResult = mockMvc.perform(post("/post/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestSavePostDto)))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        Map<String, Long> responseMap = objectMapper.readValue(response, new TypeReference<>() {
        });

        Long postId = responseMap.get("id");
        log.info("{}", postId);

        mockMvc.perform(get("/post/{postId}", postId))
                .andExpect(status().isOk());
    }

}