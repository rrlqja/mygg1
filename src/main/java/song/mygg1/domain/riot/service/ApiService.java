package song.mygg1.domain.riot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import song.mygg1.domain.common.exception.MyggException;
import song.mygg1.domain.common.exception.riot.RiotApiException;
import song.mygg1.domain.riot.dto.MatchDto;
import song.mygg1.domain.riot.entity.Account;
import song.mygg1.domain.riot.entity.Matches;
import song.mygg1.domain.riot.entity.RiotApiEndpoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiService {
    @Value("${api.riot.api-key}")
    private String riotApiKey;
    @Value("${api.riot.base-url}")
    private String riotBaseUrl;

    private final RestTemplate restTemplate;

    public Optional<Account> getAccount(String gameName, String tagLine) {
        return doExchange(RiotApiEndpoint.GET_ACCOUNT, HttpMethod.GET, Account.class, Map.of("gameName", gameName, "tagLine", tagLine));
    }

    public List<String> getMatches(String puuid, Integer start, Integer count) {
        ParameterizedTypeReference<List<String>> typeRef = new ParameterizedTypeReference<>() {};

        Optional<List<String>> optionalMatchIdList = doExchange(RiotApiEndpoint.GET_MATCHES, HttpMethod.GET, typeRef, Map.of("puuid", puuid, "start", start, "count", count));

        return optionalMatchIdList.orElseGet(ArrayList::new);
    }

    public List<String> getMatches(String puuid) {
        ParameterizedTypeReference<List<String>> typeRef = new ParameterizedTypeReference<>() {};

        Optional<List<String>> optionalMatchIdList = doExchange(RiotApiEndpoint.GET_MATCHES, HttpMethod.GET, typeRef, Map.of("puuid", puuid, "start", 0, "count", 20));

        return optionalMatchIdList.orElseGet(ArrayList::new);
    }

    public Optional<MatchDto> getMatchDetail(String matchId) {
        return doExchange(RiotApiEndpoint.GET_MATCH, HttpMethod.GET, MatchDto.class, Map.of("matchId", matchId));
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

    private <T> Optional<T> doExchange(RiotApiEndpoint endPoint, HttpMethod method, ParameterizedTypeReference<T> type, Map<String, ?> param) {
        try {
            ResponseEntity<T> response = restTemplate.exchange(
                    riotBaseUrl + endPoint.getPath(),
                    method,
                    getHttpEntity(),
                    type,
                    param
            );

            return Optional.ofNullable(response.getBody());
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        } catch (RestClientException e) {
            throw new RiotApiException("잘못된 요청입니다.");
        } catch (Exception e) {
            throw new MyggException("예상하지 못한 오류가 발생하였습니다.");
        }
    }

    private <T> Optional<T> doExchange(RiotApiEndpoint endPoint, HttpMethod method, Class<T> type, Map<String, ?> param) {
        try {
            ResponseEntity<T> response = restTemplate.exchange(
                    riotBaseUrl + endPoint.getPath(),
                    method,
                    getHttpEntity(),
                    type,
                    param
            );

            return Optional.ofNullable(response.getBody());
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        } catch (RestClientException e) {
            throw new RiotApiException("잘못된 요청입니다.");
        } catch (Exception e) {
            throw new MyggException("예상하지 못한 오류가 발생하였습니다.");
        }
    }
}
