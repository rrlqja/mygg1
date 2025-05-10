package song.mygg1.domain.riot.service.recentsearch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecentSearchService {
    private final RedisTemplate<String, String> redis;

    private static final int MAX_HISTORY = 10;

    public void add(String sessionId, String gameName, String tagLine) {
        String key = "search:recent:" + sessionId;
        String member = gameName + "#" + tagLine;
        double score  = System.currentTimeMillis();

        redis.opsForZSet().add(key, member, score);

        Long size = redis.opsForZSet().size(key);
        if (size != null && size > MAX_HISTORY) {
            redis.opsForZSet().removeRange(key, 0, size - MAX_HISTORY - 1);
        }
    }

    public List<String> get(String sessionId) {
        String key = "search:recent:" + sessionId;
        Set<String> range = redis.opsForZSet().reverseRange(key, 0, MAX_HISTORY - 1);
        return (range == null) ? List.of() : new ArrayList<>(range);
    }
}
