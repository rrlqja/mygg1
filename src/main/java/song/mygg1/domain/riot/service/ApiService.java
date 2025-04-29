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
import song.mygg1.domain.riot.dto.account.AccountDto;
import song.mygg1.domain.riot.dto.league.LeagueEntryDto;
import song.mygg1.domain.riot.dto.match.MatchDto;
import song.mygg1.domain.riot.dto.summoner.SummonerDto;
import song.mygg1.domain.riot.entity.RiotApiEndpoint;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static song.mygg1.domain.riot.entity.RiotApiEndpoint.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiService {
    @Value("${api.riot.api-key}")
    private String riotApiKey;
    @Value("${api.riot.asia-url}")
    private String riotAsiaUrl;
    @Value("${api.riot.kr-url}")
    private String riotKrUrl;

    private final RestTemplate restTemplate;

    public Optional<AccountDto> getAccount(String gameName, String tagLine) {
        return doExchange(riotAsiaUrl, GET_ACCOUNT, HttpMethod.GET, AccountDto.class, Map.of("gameName", gameName, "tagLine", tagLine));
    }

    public List<String> getMatches(String puuid, Integer start, Integer count) {
        ParameterizedTypeReference<List<String>> typeRef = new ParameterizedTypeReference<>() {};

        return doExchange(riotAsiaUrl, GET_MATCHES, HttpMethod.GET, typeRef, Map.of("puuid", puuid, "start", start, "count", count))
                .orElseGet(ArrayList::new);
    }

    public List<String> getMatches(String puuid) {
        ParameterizedTypeReference<List<String>> typeRef = new ParameterizedTypeReference<>() {};

        return doExchange(riotAsiaUrl, GET_MATCHES, HttpMethod.GET, typeRef, Map.of("puuid", puuid, "start", 0, "count", 20))
                .orElseGet(ArrayList::new);
    }

    public Optional<MatchDto> getMatchDetail(String matchId) {
        return doExchange(riotAsiaUrl, GET_MATCH, HttpMethod.GET, MatchDto.class, Map.of("matchId", matchId));
    }

    public Optional<SummonerDto> getSummoner(String puuid) {
        return doExchange(riotKrUrl, GET_SUMMONER, HttpMethod.GET, SummonerDto.class, Map.of("puuid", puuid));
    }

    public Set<LeagueEntryDto> getLeagueEntry(String puuid) {
        ParameterizedTypeReference<List<LeagueEntryDto>> typeRef = new ParameterizedTypeReference<>() {};

        List<LeagueEntryDto> leagueEntryList = doExchange(riotKrUrl, GET_LEAGUE_ENTRY, HttpMethod.GET, typeRef, Map.of("puuid", puuid))
                .orElseGet(List::of);

        return new HashSet<>(leagueEntryList);
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

    private <T> Optional<T> doExchange(String baseUrl, RiotApiEndpoint endPoint, HttpMethod method, ParameterizedTypeReference<T> type, Map<String, ?> param) {
        try {
            ResponseEntity<T> response = restTemplate.exchange(
                    baseUrl + endPoint.getPath(),
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

    private <T> Optional<T> doExchange(String baseUrl, RiotApiEndpoint endPoint, HttpMethod method, Class<T> type, Map<String, ?> param) {
        try {
            ResponseEntity<T> response = restTemplate.exchange(
                    baseUrl + endPoint.getPath(),
                    method,
                    getHttpEntity(),
                    type,
                    param
            );

            return Optional.ofNullable(response.getBody());
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        } catch (RestClientException e) {
            throw new RiotApiException("잘못된 요청입니다.", e);
        } catch (Exception e) {
            throw new MyggException("예상하지 못한 오류가 발생하였습니다.", e);
        }
    }
}
