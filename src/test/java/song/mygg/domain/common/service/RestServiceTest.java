package song.mygg.domain.common.service;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;
import song.mygg.domain.common.entity.CommonEntity;
import song.mygg.domain.loa.entity.Adventurer;
import song.mygg.domain.loa.util.LoaUrl;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@TestPropertySource(properties = {
        "JASYPT_ENCRYPTOR_PASSWORD=song"
})
class RestServiceTest {
    @Autowired
    RestService restService;
    @Autowired
    RestTemplate restTemplate;
    @Value("${loa.authorization}")
    private String loaAuthorization;

    @Test
    void successFindJSPH() {
        Optional<CommonEntity> response = restService.getRest("https://jsonplaceholder.typicode.com/todos/1", CommonEntity.class);

        assertThat(response.isPresent())
                .isTrue();
    }

    @Test
    void failFindJSPH() {
        Assertions.assertThatThrownBy(()-> restService.getRest("testUrl", CommonEntity.class))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void sslTest() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Authorization", "bearer " + loaAuthorization);
        HttpEntity<Adventurer> httpEntity = new HttpEntity<>(headers);

//        Adventurer response = restTemplate.exchange(
//                LoaUrl.GET_ARMORIES_PROFILE.buildUrl(Map.of("characterName", "필례")),
//                HttpMethod.GET,
//                httpEntity,
//                Adventurer.class
//        ).getBody();

        String response = restTemplate.exchange(
                LoaUrl.GET_ARMORIES_PROFILE.buildUrl(Map.of("characterName", "필례")),
                HttpMethod.GET,
                httpEntity,
                String.class
        ).getBody();

        log.info("reponse = {}", response);

//        String url = LoaUrl.GET_ARMORIES_PROFILE.buildUrl(Map.of("characterName", "주다영"));
//        System.out.println("url = " + url);
    }

}