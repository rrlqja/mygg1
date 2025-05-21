package song.mygg1.domain.riot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;
import song.mygg1.domain.riot.service.datadragon.DataDragonService;
import song.mygg1.domain.riot.dto.item.ItemDto;
import song.mygg1.domain.riot.dto.item.ItemResponseDto;
import song.mygg1.domain.riot.dto.timeline.TimelineDto;

import java.util.Map;

@Slf4j
@SpringBootTest
@TestPropertySource(properties = {
        "spring.profiles.active=test",
        "JASYPT_ENCRYPTOR_PASSWORD=test"
})
class ApiServiceTest {
    @Autowired
    ApiService apiService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    DataDragonService dataDragonService;

//    @Test
//    void apiTest() throws JsonProcessingException {
////        ResponseEntity<Object> response = restTemplate.exchange(
////                riotBaseUrl + "/lol/match/v5/matches/{matchId}",
////                HttpMethod.GET,
////                getHttpEntity(),
////                Object.class,
////                Map.of("matchId", "KR_7602525834")
////        );
////        Object body = response.getBody();
//
//        ResponseEntity<MatchDto> response = restTemplate.exchange(
//                riotBaseUrl + "/lol/match/v5/matches/{matchId}",
//                HttpMethod.GET,
//                getHttpEntity(),
//                MatchDto.class,
//                Map.of("matchId", "KR_7602525834")
//        );
//        MatchDto body = response.getBody();
//
//        String res = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(body);
//
//        log.info("{}", res);
//    }
//
//    private HttpEntity<Object> getHttpEntity() {
//        return new HttpEntity<>(getHeaders());
//    }
//
//    private HttpHeaders getHeaders() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("X-Riot-Token", riotApiKey);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        return headers;
//    }

    @Test
    void successGetAccount() {
        Assertions.assertDoesNotThrow(() -> apiService.getAccount("hide on bush", "kr1"));
    }

    @Test
    void getMatchTimeLine() throws JsonProcessingException {
        TimelineDto res = apiService.getMatchTimeline("KR_7644097059").get();

        log.info(objectMapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(res));
    }

    @Test
    void getItem() throws JsonProcessingException {
        JsonNode root = apiService.getItemJson(dataDragonService.getVersion()).get();

        ItemResponseDto itemResponse = objectMapper.treeToValue(root, ItemResponseDto.class);

        Map<String, ItemDto> data = itemResponse.getData();
        for (String key : data.keySet()) {
            log.info("item: " + data.get(key));
        }
    }
}