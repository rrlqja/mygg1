package song.mygg1.domain.redis.service.match;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchCacheLimiterService {
    private final RedisTemplate<String, String> redis;
    private static final String INDEX_KEY = "match:detail:keys";
    private static final int    MAX_KEYS  = 100;

    public void trackAndTrim(String key) {
        long ts = System.currentTimeMillis();
        redis.opsForZSet().add(INDEX_KEY, key, ts);
        Long size = redis.opsForZSet().size(INDEX_KEY);
        if (size != null && size > MAX_KEYS) {
            Set<String> toRemove = redis.opsForZSet()
                    .range(INDEX_KEY, 0, size - MAX_KEYS - 1);
            if (toRemove != null && !toRemove.isEmpty()) {
                toRemove.forEach(redis::delete);
                redis.opsForZSet().remove(INDEX_KEY, toRemove.toArray(new String[0]));
            }
        }
    }
}
