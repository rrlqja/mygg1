package song.mygg1.domain.riot.service.match;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import song.mygg1.domain.redis.service.CacheService;
import song.mygg1.domain.riot.dto.match.ChampionWinRatePerDateDto;
import song.mygg1.domain.riot.repository.match.ParticipantJpaRepository;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParticipantService {
    private final ParticipantJpaRepository participantRepository;
    private final CacheService<List<ChampionWinRatePerDateDto>> cacheService;

    private static final Duration WIN_RATE_PER_DATE_TTL = Duration.ofDays(1L);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<ChampionWinRatePerDateDto> getChampionWinRatePerDate(String championName) {
        String key = "champion:winRatePerDate:" + championName;

        return cacheService.getOrLoad(
                key,
                () -> getChampionWinRate(championName),
                WIN_RATE_PER_DATE_TTL
        );
    }

    private List<ChampionWinRatePerDateDto> getChampionWinRate(String championName) {
        ZoneId kst = ZoneId.of("Asia/Seoul");
        LocalDate yesterday = LocalDate.now(kst).minusDays(1);
        LocalDate startDate = yesterday.minusDays(6);

        Instant startInstant = startDate
                .atStartOfDay(kst)
                .toInstant();
        Instant endInstant = yesterday
                .atTime(LocalTime.MAX)
                .atZone(kst)
                .toInstant();

        long startMs = startInstant.toEpochMilli();
        long endMs = endInstant.toEpochMilli();

        return participantRepository.findChampionDailyWinRate(
                championName,
                startMs,
                endMs
        );
    }
}
