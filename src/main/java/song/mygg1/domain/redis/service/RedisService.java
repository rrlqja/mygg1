package song.mygg1.domain.redis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import song.mygg1.domain.common.service.DataDragonService;
import song.mygg1.domain.riot.dto.champion.ChampionRotationsDto;
import song.mygg1.domain.riot.dto.league.LeagueItemDto;
import song.mygg1.domain.riot.dto.league.LeagueListDto;
import song.mygg1.domain.riot.entity.league.LeagueQueue;
import song.mygg1.domain.riot.service.league.LeagueService;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisService {
    private static final int MAX_HISTORY = 10;
    private final StringRedisTemplate redis;
    private final DataDragonService dataDragonService;
    private final LeagueService leagueService;

    public void get(String sessionId) {
        String os = redis.opsForValue().get("os");

        log.info("redis os: {}", os);
    }

    public void addRecentSearch(String sessionId, String gameName, String tagLine) {
        String key = "recent:search:" + sessionId;
        String search = gameName + "#" + tagLine;
        double score = System.currentTimeMillis();

        redis.opsForZSet().add(key, search, score);

        Long size = redis.opsForZSet().size(key);
        if (size != null && size > MAX_HISTORY) {
            redis.opsForZSet().removeRange(key, 0, size - MAX_HISTORY - 1);
        }
    }

    public List<String> getRecentSearch(String sessionId) {
        String key = "recent:search:" + sessionId;

        Set<String> range = redis.opsForZSet().reverseRange(key, 0, MAX_HISTORY - 1);
        return (range == null) ? Collections.emptyList() : new ArrayList<>(range);
    }

    public ChampionRotationsDto setChampionRotations() {
        ChampionRotationsDto championRotations = dataDragonService.getChampionRotations();

        setFreeChampion(championRotations.getFreeChampionIds());
        setFreeChampionForNewP(championRotations.getFreeChampionIdsForNewPlayers());

        return championRotations;
    }

    private void setFreeChampion(List<Integer> freeChampionIds) {
        String key = "rotation:freeChampion";
        redis.delete(key);
        List<String> value = freeChampionIds.stream()
                .map(String::valueOf)
                .toList();
        redis.opsForList().rightPushAll(key, value);
        redis.expire(key, Duration.ofDays(7L));
    }

    private void setFreeChampionForNewP(List<Integer> freeChampionIdsForNewPlayers) {
        String key = "rotation:freeChampion:forNewP";
        redis.delete(key);
        List<String> value = freeChampionIdsForNewPlayers.stream()
                .map(String::valueOf)
                .toList();
        redis.opsForList().rightPushAll(key, value);
        redis.expire(key, Duration.ofDays(7L));
    }

    public List<Long> getFreeChampion() {
        String key = "rotation:freeChampion";
        List<String> value = redis.opsForList().range(key, 0, -1);
        return value.stream().map(Long::valueOf).toList();
    }

    public List<Long> getFreeChampionForNewP() {
        String key = "rotation:freeChampion:forNewP";
        List<String> value = redis.opsForList().range(key, 0, -1);
        return value.stream().map(Long::valueOf).toList();
    }

    public LeagueListDto setChallengerLeague() {
        LeagueListDto leagueList = leagueService.getLeagueList(LeagueQueue.RANKED_SOLO_5x5.getQueue());
        String key = "league:challenger";
        redis.delete(key);
        redis.opsForList().rightPushAll(key, leagueList.getEntries().stream().sorted(Comparator.comparingInt(LeagueItemDto::getLeaguePoints).reversed()).map(LeagueItemDto::getSummonerId).limit(10).toList());
        redis.expire(key, Duration.ofDays(1L));

        return leagueList;
    }

    public List<String> getChallengerLeagueBySummonerId() {
        String key = "league:challenger";
        return redis.opsForList().range(key, 0, -1);
    }
}
