package song.mygg1.config;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.local.LocalBucketBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Slf4j
@Configuration
public class RateLimiterConf {

    @Bean(name = "baseLimiter")
    public Bucket baseLimiter() {
        return getBaseBucket().build();
    }

    @Bean(name = "rotationLimiter")
    public Bucket rotationLimiter() {
        return getBaseBucket()
                .addLimit(limit -> limit.capacity(30).refillIntervally(30, Duration.ofSeconds(10)))
                .addLimit(limit -> limit.capacity(500).refillIntervally(100, Duration.ofSeconds(1200)))
                .build();
    }

    @Bean(name = "accountLimiter")
    public Bucket accountLimiter() {
        return getBaseBucket()
                .addLimit(limit -> limit.capacity(1000).refillIntervally(100, Duration.ofSeconds(120)))
                .build();
    }

    @Bean(name = "summonerLimiter")
    public Bucket summonerLimiter() {
        return getBaseBucket()
                .addLimit(limit -> limit.capacity(1200).refillIntervally(100, Duration.ofSeconds(120)))
                .build();
    }

    @Bean(name = "leagueLimiter")
    public Bucket leagueLimiter() {
        return getBaseBucket()
                .addLimit(limit -> limit.capacity(18000).refillIntervally(100, Duration.ofSeconds(20)))
                .build();
    }

    @Bean(name = "matchLimiter")
    public Bucket matchLimiter() {
        return getBaseBucket()
                .addLimit(limit -> limit.capacity(18000).refillIntervally(100, Duration.ofSeconds(20)))
                .build();
    }

    private LocalBucketBuilder getBaseBucket() {
        return Bucket.builder()
                .addLimit(limit -> limit.capacity(20).refillIntervally(20, Duration.ofSeconds(2)))
                .addLimit(limit -> limit.capacity(100).refillIntervally(100, Duration.ofSeconds(240)));
    }

}
