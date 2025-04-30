package song.mygg1.domain.redis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisService {
    private static final int MAX_HISTORY = 10;
    private final StringRedisTemplate redis;

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
}
