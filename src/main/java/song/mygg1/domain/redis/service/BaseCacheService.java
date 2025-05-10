package song.mygg1.domain.redis.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class BaseCacheService<T> implements CacheService<T> {
    private final RedisTemplate<String, String> redis;
    private final ObjectMapper mapper;
    private final JavaType type;

    @Override
    public Optional<T> get(String key) {
        String json = redis.opsForValue().get(key);
        if (json == null) return Optional.empty();
        try {
            log.info("read key: {}", key);
            return Optional.of(mapper.readValue(json, type));
        } catch (IOException e) {
            log.warn("Cache deserialization failed, evicting key: {}", key, e);
            redis.delete(key);
            return Optional.empty();
        }
    }

    @Override
    public void put(String key, Object value, Duration ttl) {
        try {
            log.info("write key: {}", key);
            String json = mapper.writeValueAsString(value);
            redis.opsForValue().set(key, json, ttl.toMillis(), TimeUnit.MILLISECONDS);
        } catch (JsonProcessingException e) {
            log.error("Cache serialization error for key: {}", key, e);
        }
    }

    @Override
    public void evict(String key) {
        log.info("evict key: {}", key);
        redis.delete(key);
    }
}
