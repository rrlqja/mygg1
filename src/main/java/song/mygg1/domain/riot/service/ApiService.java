package song.mygg1.domain.riot.service;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.bucket4j.Bucket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import song.mygg1.domain.common.exception.MyggException;
import song.mygg1.domain.common.exception.riot.riotapi.RiotApiException;
import song.mygg1.domain.riot.dto.account.AccountDto;
import song.mygg1.domain.riot.dto.champion.ChampionRotationsDto;
import song.mygg1.domain.riot.dto.league.LeagueEntryDto;
import song.mygg1.domain.riot.dto.league.LeagueListDto;
import song.mygg1.domain.riot.dto.champion.ChampionMasteryDto;
import song.mygg1.domain.riot.dto.match.MatchDto;
import song.mygg1.domain.riot.dto.summoner.SummonerDto;
import song.mygg1.domain.riot.dto.timeline.TimelineDto;

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
@EnableRetry
@Retryable(
        retryFor = {RestClientException.class, },
        backoff = @Backoff(delay = 240000, multiplier = 2),
        maxAttempts = 4
)
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
    private final Bucket baseBucket;
    private final Bucket rotationLimiter;
    private final Bucket accountLimiter;
    private final Bucket summonerLimiter;
    private final Bucket leagueLimiter;
    private final Bucket matchLimiter;

    public ApiService(RestTemplate restTemplate, @Qualifier("baseLimiter") Bucket baseBucket, @Qualifier("rotationLimiter") Bucket rotationLimiter,
                      @Qualifier("accountLimiter") Bucket accountLimiter, @Qualifier("summonerLimiter") Bucket summonerLimiter,
                      @Qualifier("leagueLimiter") Bucket leagueLimiter, @Qualifier("matchLimiter") Bucket matchLimiter) {
        this.baseBucket = baseBucket;
        this.rotationLimiter = rotationLimiter;
        this.restTemplate = restTemplate;
        this.accountLimiter = accountLimiter;
        this.summonerLimiter = summonerLimiter;
        this.leagueLimiter = leagueLimiter;
        this.matchLimiter = matchLimiter;
    }

    public Optional<String[]> getDataDragonVersions() {
        return doExchange(baseBucket, riotDataDragonUrl, GET_DATA_DRAGON_VERSION.getPath(), HttpMethod.GET, String[].class, Map.of());
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

    public Optional<JsonNode> getChampionJson(String version) {
        return doExchange(
                baseBucket,
                riotDataDragonUrl,
                GET_CHAMPION_JSON.getPath(),
                HttpMethod.GET,
                JsonNode.class,
                Map.of("version", version)
        );
    }

    public Optional<JsonNode> getItemJson(String version) {
        return doExchange(
                baseBucket,
                riotDataDragonUrl,
                GET_ITEM_JSON.getPath(),
                HttpMethod.GET,
                JsonNode.class,
                Map.of("version", version)
        );
    }

    public ChampionRotationsDto getChampionRotations() {
        return doExchange(baseBucket, riotKrUrl, GET_CHAMPION_ROTATIONS.getPath(), HttpMethod.GET, ChampionRotationsDto.class, Map.of())
                .orElseThrow(RiotApiException::new);
    }

    public Optional<byte[]> getSpellIcon(String version, int spellId) {
        Optional<JsonNode> optionalSummoner = doExchange(
                baseBucket,
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
        String group = null, full = null;
        for (Iterator<Map.Entry<String, JsonNode>> it = dataNode.fields(); it.hasNext(); ) {
            JsonNode node = it.next().getValue();
            if (node.get("key").asInt() == spellId) {
                JsonNode image = node.get("image");
                group = image.get("group").asText();
                full  = image.get("full").asText();
                break;
            }
        }
        if (group == null || full == null) {
            return Optional.empty();
        }

        return fetchIcon(
                GET_ICON.getPath(),
                Map.of("version", version,
                        "group",   group,
                        "full",    full)
        );
    }

    private Optional<byte[]> fetchIcon(String path, Map<String, ?> uriVars) {
        try {
            return doExchange(
                    baseBucket,
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
        return doExchange(baseBucket, riotAsiaUrl, GET_ACCOUNT.getPath(), HttpMethod.GET, AccountDto.class, Map.of("gameName", gameName, "tagLine", tagLine));
    }

    public Optional<AccountDto> getAccount(String puuid) {
        return doExchange(baseBucket, riotAsiaUrl, GET_ACCOUNT_BY_PUUID.getPath(), HttpMethod.GET, AccountDto.class, Map.of("puuid", puuid));
    }

    public List<String> getMatches(String puuid, Integer start, Integer count) {
        ParameterizedTypeReference<List<String>> typeRef = new ParameterizedTypeReference<>() {};

        return doExchange(baseBucket, riotAsiaUrl, GET_MATCHES.getPath(), HttpMethod.GET, typeRef, Map.of("puuid", puuid, "start", start, "count", count))
                .orElseGet(ArrayList::new);
    }

    public List<String> getMatches(String puuid) {
        ParameterizedTypeReference<List<String>> typeRef = new ParameterizedTypeReference<>() {};

        return doExchange(baseBucket, riotAsiaUrl, GET_MATCHES.getPath(), HttpMethod.GET, typeRef, Map.of("puuid", puuid, "start", 0, "count", 20))
                .orElseGet(ArrayList::new);
    }

    public Optional<MatchDto> getMatchDetail(String matchId) {
        return doExchange(baseBucket, riotAsiaUrl, GET_MATCH.getPath(), HttpMethod.GET, MatchDto.class, Map.of("matchId", matchId));
    }

    public Optional<TimelineDto> getMatchTimeline(String matchId) {
        return doExchange(baseBucket, riotAsiaUrl, GET_MATCH_TIMELINE.getPath(), HttpMethod.GET, TimelineDto.class, Map.of("matchId", matchId));
    }

    public Optional<SummonerDto> getSummoner(String puuid) {
        return doExchange(baseBucket, riotKrUrl, GET_SUMMONER.getPath(), HttpMethod.GET, SummonerDto.class, Map.of("puuid", puuid));
    }

    public Optional<SummonerDto> getSummonerBySummonerId(String summonerId) {
        return doExchange(baseBucket, riotKrUrl, GET_SUMMONER_BY_SUMMONER_ID.getPath(), HttpMethod.GET, SummonerDto.class, Map.of("summonerId", summonerId));
    }

    public Set<LeagueEntryDto> getLeagueEntry(String puuid) {
        ParameterizedTypeReference<List<LeagueEntryDto>> typeRef = new ParameterizedTypeReference<>() {};

        List<LeagueEntryDto> leagueEntryList = doExchange(baseBucket, riotKrUrl, GET_LEAGUE_ENTRY.getPath(), HttpMethod.GET, typeRef, Map.of("puuid", puuid))
                .orElseGet(List::of);

        return new HashSet<>(leagueEntryList);
    }

    public Optional<LeagueListDto> getChallengerLeagueList(String queue) {
        return doExchange(baseBucket, riotKrUrl, GET_CHALLENGER_LEAGUE_LIST.getPath(), HttpMethod.GET, LeagueListDto.class, Map.of("queue", queue));
    }

    public Optional<LeagueListDto> getGrandmasterLeagueList(String queue) {
        return doExchange(baseBucket, riotKrUrl, GET_GRANDMASTER_LEAGUE_LIST.getPath(), HttpMethod.GET, LeagueListDto.class, Map.of("queue", queue));
    }

    public Optional<LeagueListDto> getMasterLeagueList(String queue) {
        return doExchange(baseBucket, riotKrUrl, GET_MASTER_LEAGUE_LIST.getPath(), HttpMethod.GET, LeagueListDto.class, Map.of("queue", queue));
    }

    public List<ChampionMasteryDto> getChampionMastery(String puuid) {
        ParameterizedTypeReference<List<ChampionMasteryDto>> typeRef = new ParameterizedTypeReference<>() {};
        return doExchange(baseBucket, riotKrUrl, GET_CHAMPION_MASTERY.getPath(), HttpMethod.GET, typeRef, Map.of("puuid", puuid))
                .orElseGet(ArrayList::new);
    }

    public List<ChampionMasteryDto> getChampionMasteryTop(String puuid, Integer count) {
        ParameterizedTypeReference<List<ChampionMasteryDto>> typeRef = new ParameterizedTypeReference<>() {};
        return doExchange(baseBucket, riotKrUrl, GET_CHAMPION_MASTERY_TOP_BY_PUUID.getPath(), HttpMethod.GET, typeRef, Map.of("puuid", puuid, "count", count))
                .orElseGet(ArrayList::new);
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

    @Retryable(
            retryFor = {RestClientException.class, },
            backoff = @Backoff(delay = 240000, multiplier = 2)
    )
    private <T> Optional<T> doExchange(Bucket bucket, String baseUrl, String endPoint, HttpMethod method, ParameterizedTypeReference<T> type, Map<String, ?> param) {
        try {
            bucket.asBlocking().consume(1);
            log.info("executing {} {} with param ({}) [bucket remaining token : {}]", method, baseUrl + endPoint, param, bucket.getAvailableTokens());

            ResponseEntity<T> response = restTemplate.exchange(
                    baseUrl + endPoint,
                    method,
                    getHttpEntity(),
                    type,
                    param
            );

            return Optional.ofNullable(response.getBody());
        } catch (RestClientException | IllegalArgumentException e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        } catch (Exception e) {
            throw new MyggException("예상하지 못한 오류가 발생하였습니다.", e);
        }
    }

    @Retryable(
            retryFor = {RestClientException.class, },
            backoff = @Backoff(delay = 240000, multiplier = 2)
    )
    private <T> Optional<T> doExchange(Bucket bucket, String baseUrl, String endPoint, HttpMethod method, Class<T> type, Map<String, ?> param) {
        try {
            bucket.asBlocking().consume(1);
            log.info("executing {} {} with param ({}) [bucket remaining token : {}]", method, baseUrl + endPoint, param, bucket.getAvailableTokens());

            ResponseEntity<T> response = restTemplate.exchange(
                    baseUrl + endPoint,
                    method,
                    getHttpEntity(),
                    type,
                    param
            );

            return Optional.ofNullable(response.getBody());
        } catch (RestClientException | IllegalArgumentException e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        } catch (Exception e) {
            throw new MyggException("예상하지 못한 오류가 발생하였습니다.", e);
        }
    }

    @Recover
    private <T> Optional<T> recover(RiotApiException e,
                                    Bucket bucket,
                                    String baseUrl,
                                    String endPoint,
                                    HttpMethod method,
                                    Class<T> type,
                                    Map<String, ?> params) {
        log.error("doExchange retry failed for {}{} params={}, error={}",
                baseUrl, endPoint, params, e.getMessage());

        return Optional.empty();
    }
}
