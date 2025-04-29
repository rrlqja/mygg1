package song.mygg1.domain.riot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import song.mygg1.domain.riot.dto.SearchDto;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@TestPropertySource(properties = {
        "spring.profiles.active=test",
        "JASYPT_ENCRYPTOR_PASSWORD=test"
})
class SearchServiceTest {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    SearchService searchService;

    @Test
    void successSearchAccount() throws JsonProcessingException {
        SearchDto search = searchService.search("hide on bush", "kr1", 0, 10);

        String searchJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(search);
        log.info("{}", searchJson);
    }

}