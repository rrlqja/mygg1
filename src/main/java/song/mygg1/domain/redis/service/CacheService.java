package song.mygg1.domain.redis.service;


import java.time.Duration;
import java.util.Optional;
import java.util.function.Supplier;

public interface CacheService<T> {
    Optional<T> get(String key);
    void put(String key, T value, Duration ttl);
    void evict(String key);

    default T getOrLoad(String key, Supplier<T> loader, Duration ttl) {
        return get(key)
                .orElseGet(() -> {
                    T data = loader.get();
                    put(key, data, ttl);
                    return data;
                });
    }
}
