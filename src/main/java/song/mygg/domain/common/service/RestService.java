package song.mygg.domain.common.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import song.mygg.domain.common.entity.CommonEntity;
import song.mygg.domain.common.exception.rest.RestException;
import song.mygg.domain.common.exception.rest.RestNotFoundException;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestService {
    private final RestTemplate restTemplate;

    @Value("${loa.authorization}")
    private String loaAuthorization;

    public <T extends CommonEntity> Optional<T> getRest(String url, Class<T> clazz) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.add("Authorization", "bearer " + loaAuthorization);
        HttpEntity<T> httpEntity = new HttpEntity<>(headers);

        try {
            T response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    httpEntity,
                    clazz
            ).getBody();
            return Optional.ofNullable(response);
        } catch (HttpClientErrorException.NotFound e) {
            throw new RestNotFoundException("데이터를 찾을 수 없습니다.");
        } catch (HttpClientErrorException e) {
            throw new RestException("rest request exception");
        } catch (Exception e) {
            throw new RuntimeException("rest exception");
        }
    }
}
