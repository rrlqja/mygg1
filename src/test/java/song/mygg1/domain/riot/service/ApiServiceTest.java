package song.mygg1.domain.riot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import song.mygg1.domain.common.exception.MyggException;
import song.mygg1.domain.common.exception.riot.RiotApiException;
import song.mygg1.domain.riot.dto.MatchDto;

import java.util.Map;
import java.util.Optional;

@Slf4j
@SpringBootTest
@TestPropertySource(properties = {
        "spring.profiles.active=test",
        "JASYPT_ENCRYPTOR_PASSWORD=test"
})
class ApiServiceTest {
    @Value("${api.riot.api-key}")
    private String riotApiKey;
    @Value("${api.riot.base-url}")
    private String riotBaseUrl;
    @Autowired
    ApiService apiService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    RestTemplate restTemplate;

    @Test
    void apiTest() throws JsonProcessingException {
//        ResponseEntity<Object> response = restTemplate.exchange(
//                riotBaseUrl + "/lol/match/v5/matches/{matchId}",
//                HttpMethod.GET,
//                getHttpEntity(),
//                Object.class,
//                Map.of("matchId", "KR_7602525834")
//        );
//        Object body = response.getBody();

        ResponseEntity<MatchDto> response = restTemplate.exchange(
                riotBaseUrl + "/lol/match/v5/matches/{matchId}",
                HttpMethod.GET,
                getHttpEntity(),
                MatchDto.class,
                Map.of("matchId", "KR_7602525834")
        );
        MatchDto body = response.getBody();

        String res = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(body);

        log.info("{}", res);
    }

    private HttpEntity<Object> getHttpEntity() {
        return new HttpEntity<>(getHeaders());
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Riot-Token", riotApiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    @Test
    void successGetAccount() {
        Assertions.assertDoesNotThrow(() -> apiService.getAccount("hide on bush", "kr1"));
    }
}