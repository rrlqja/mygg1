package song.mygg1.domain.riot.service.match;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import song.mygg1.domain.redis.service.CacheService;
import song.mygg1.domain.riot.dto.match.info.ChampionUsageDto;
import song.mygg1.domain.riot.repository.match.InfoJpaRepository;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InfoService {
    private final InfoJpaRepository infoRepository;
    private final CacheService<List<ChampionUsageDto>> championUsageCacheService;

    private static final ZoneId kst = ZoneId.of("Asia/Seoul");
    private static final Duration WIN_RATE_TTL = Duration.ofDays(1L);


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<ChampionUsageDto> getChampionUsage(Long championId) {
        String key = "champion:usage:" + championId;

        return championUsageCacheService.getOrLoad(
                key,
                () -> getUsage(championId),
                WIN_RATE_TTL
        );
    }

    private List<ChampionUsageDto> getUsage(Long championId) {
        LocalDate yesterday = LocalDate.now(kst).minusDays(1);
        LocalDate startDate = yesterday.minusDays(6);

        Instant startInstant = startDate
                .atStartOfDay(kst)
                .toInstant();
        Instant endInstant = yesterday
                .atTime(LocalTime.MAX)
                .atZone(kst)
                .toInstant();

        long start = startInstant.toEpochMilli();
        long end = endInstant.toEpochMilli();

        return infoRepository.findChampionUsage(
                championId,
                start,
                end
        );
    }
}
