package song.mygg1.domain.redis.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KeyEvictor implements ApplicationListener<ContextRefreshedEvent> {
    private final RedisTemplate<String, ?> redis;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        redis.keys("recent:search:*")
                .forEach(redis::delete);
    }
}
