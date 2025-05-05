package song.mygg1.domain.riot.service;

import com.fasterxml.jackson.databind.JsonNode;
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
import song.mygg1.domain.common.exception.riot.riotapi.RiotApiException;
import song.mygg1.domain.riot.dto.account.AccountDto;
import song.mygg1.domain.riot.dto.league.LeagueEntryDto;
import song.mygg1.domain.riot.dto.match.MatchDto;
import song.mygg1.domain.riot.dto.summoner.SummonerDto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static song.mygg1.domain.riot.entity.RiotApiEndpoint.*;
import static song.mygg1.domain.riot.entity.RiotDataDragonEndpoint.*;

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
    @Value("${api.riot.data-dragon}")
    private String riotDataDragonUrl;

    private final RestTemplate restTemplate;

    public Optional<String[]> getDataDragonVersions() {
        return doExchange(riotDataDragonUrl, GET_DATA_DRAGON_VERSION.getPath(), HttpMethod.GET, String[].class, Map.of());
    }

    public Optional<byte[]> getProfileIcon(String version, int iconId) {
        return fetchIcon(
                GET_PROFILE_ICON.getPath(),
                Map.of("version", version, "iconId", iconId)
        );
    }
    public Optional<byte[]> getChampionIcon(String version, String championName) {
        return fetchIcon(
                GET_CHAMPION.getPath(),
                Map.of("version", version, "championName", championName)
        );
    }

    public Optional<byte[]> getItemIcon(String version, int itemId) {
        return fetchIcon(
                GET_ITEM.getPath(),
                Map.of("version", version, "itemId", itemId)
        );
    }

    public Optional<byte[]> getSpellIcon(String version, int spellId) {
        Optional<JsonNode> optionalSummoner = doExchange(
                riotDataDragonUrl,
                GET_SUMMONER_JSON.getPath(),
                HttpMethod.GET,
                JsonNode.class,
                Map.of("version", version)
        );
        if (optionalSummoner.isEmpty()) {
            return Optional.empty();
        }

        JsonNode dataNode = optionalSummoner.get().get("data");
        String group = null, icon = null;
        for (Iterator<Map.Entry<String, JsonNode>> it = dataNode.fields(); it.hasNext(); ) {
            JsonNode node = it.next().getValue();
            if (node.get("key").asInt() == spellId) {
                JsonNode image = node.get("image");
                group = image.get("group").asText();
                icon  = image.get("full").asText();
                break;
            }
        }
        if (group == null || icon == null) {
            return Optional.empty();
        }

        return fetchIcon(
                GET_SPELL_ICON.getPath(),
                Map.of("version", version,
                        "group",   group,
                        "full",    icon)
        );
    }

    private Optional<byte[]> fetchIcon(String path, Map<String, ?> uriVars) {
        try {
            return doExchange(
                    riotDataDragonUrl,
                    path,
                    HttpMethod.GET,
                    byte[].class,
                    uriVars
            );
        } catch (RiotApiException e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    public Optional<AccountDto> getAccount(String gameName, String tagLine) {
        return doExchange(riotAsiaUrl, GET_ACCOUNT.getPath(), HttpMethod.GET, AccountDto.class, Map.of("gameName", gameName, "tagLine", tagLine));
    }

    public Optional<AccountDto> getAccount(String puuid) {
        return doExchange(riotAsiaUrl, GET_ACCOUNT_BY_PUUID.getPath(), HttpMethod.GET, AccountDto.class, Map.of("puuid", puuid));
    }

    public List<String> getMatches(String puuid, Integer start, Integer count) {
        ParameterizedTypeReference<List<String>> typeRef = new ParameterizedTypeReference<>() {};

        return doExchange(riotAsiaUrl, GET_MATCHES.getPath(), HttpMethod.GET, typeRef, Map.of("puuid", puuid, "start", start, "count", count))
                .orElseGet(ArrayList::new);
    }

    public List<String> getMatches(String puuid) {
        ParameterizedTypeReference<List<String>> typeRef = new ParameterizedTypeReference<>() {};

        return doExchange(riotAsiaUrl, GET_MATCHES.getPath(), HttpMethod.GET, typeRef, Map.of("puuid", puuid, "start", 0, "count", 20))
                .orElseGet(ArrayList::new);
    }

    public Optional<MatchDto> getMatchDetail(String matchId) {
        return doExchange(riotAsiaUrl, GET_MATCH.getPath(), HttpMethod.GET, MatchDto.class, Map.of("matchId", matchId));
    }

    public Optional<SummonerDto> getSummoner(String puuid) {
        return doExchange(riotKrUrl, GET_SUMMONER.getPath(), HttpMethod.GET, SummonerDto.class, Map.of("puuid", puuid));
    }

    public Set<LeagueEntryDto> getLeagueEntry(String puuid) {
        ParameterizedTypeReference<List<LeagueEntryDto>> typeRef = new ParameterizedTypeReference<>() {};

        List<LeagueEntryDto> leagueEntryList = doExchange(riotKrUrl, GET_LEAGUE_ENTRY.getPath(), HttpMethod.GET, typeRef, Map.of("puuid", puuid))
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

    private <T> Optional<T> doExchange(String baseUrl, String endPoint, HttpMethod method, ParameterizedTypeReference<T> type, Map<String, ?> param) {
        try {
            ResponseEntity<T> response = restTemplate.exchange(
                    baseUrl + endPoint,
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

    private <T> Optional<T> doExchange(String baseUrl, String endPoint, HttpMethod method, Class<T> type, Map<String, ?> param) {
        try {
            ResponseEntity<T> response = restTemplate.exchange(
                    baseUrl + endPoint,
                    method,
                    getHttpEntity(),
                    type,
                    param
            );

            return Optional.ofNullable(response.getBody());
        } catch (HttpClientErrorException.NotFound e) {
            return Optional.empty();
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        } catch (RestClientException e) {
            throw new RiotApiException("잘못된 요청입니다.", e);
        } catch (Exception e) {
            throw new MyggException("예상하지 못한 오류가 발생하였습니다.", e);
        }
    }
}
