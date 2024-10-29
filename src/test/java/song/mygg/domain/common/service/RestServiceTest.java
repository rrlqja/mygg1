package song.mygg.domain.common.service;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import song.mygg.domain.common.entity.CommonEntity;
import song.mygg.domain.loa.entity.Adventurer;
import song.mygg.domain.loa.service.LoaUrl;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
class RestServiceTest {
    @Autowired
    RestService restService;
    @Autowired
    RestTemplate restTemplate;

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
        headers.add("Authorization", "bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6IktYMk40TkRDSTJ5NTA5NWpjTWk5TllqY2lyZyIsImtpZCI6IktYMk40TkRDSTJ5NTA5NWpjTWk5TllqY2lyZyJ9.eyJpc3MiOiJodHRwczovL2x1ZHkuZ2FtZS5vbnN0b3ZlLmNvbSIsImF1ZCI6Imh0dHBzOi8vbHVkeS5nYW1lLm9uc3RvdmUuY29tL3Jlc291cmNlcyIsImNsaWVudF9pZCI6IjEwMDAwMDAwMDAwMDE1NDUifQ.fJilv6MmSknimjscfoC4BCqZ8J1O7dWZQS639bgOKsY5lUgH3ospl-yPUWK9hoTjN228JL-zZ2fcygi5KU-ReKrXn2Jbd8TiWdGVakGD-Q4BB5qG4u-eB9Un408Z6dVGYpK0hcAm2Unx2H5KF0Ce9VhhSV6xCdoscDlNHwA179aOMlotKOXwJUnnwF8W_raDhqy7hcijIsqArqAwHY2wNaMgILcalVQMwsk47s4oQ0Vy_kKRJ9YIfGj_s6xavGDudVGfN8cFLQS1a1oqOYk-GtqmIi1E-S7-jchnN5VNCNhjx9g5x63SzWDvM4dbBJAJaYYyNBTziC8N4n17HtYLhQ");
        HttpEntity<Adventurer> httpEntity = new HttpEntity<>(headers);

        Adventurer response = restTemplate.exchange(
                LoaUrl.GET_ARMORIES_PROFILE.buildUrl(Map.of("characterName", "필례")),
                HttpMethod.GET,
                httpEntity,
                Adventurer.class
        ).getBody();

//        String url = LoaUrl.GET_ARMORIES_PROFILE.buildUrl(Map.of("characterName", "주다영"));
//        System.out.println("url = " + url);
    }

}