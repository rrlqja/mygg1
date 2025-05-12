package song.mygg1.domain.riot.service.match;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import song.mygg1.domain.redis.service.CacheService;
import song.mygg1.domain.riot.dto.match.participant.ChampionWinRatePerDateDto;
import song.mygg1.domain.riot.dto.match.participant.WinRateDto;
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
    private final CacheService<List<ChampionWinRatePerDateDto>> championWinRateCacheService;
    private final CacheService<List<WinRateDto>> winRateDailyCacheService;

    private static final ZoneId kst = ZoneId.of("Asia/Seoul");
    private static final Duration WIN_RATE_TTL = Duration.ofDays(1L);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<ChampionWinRatePerDateDto> getChampionWinRatePerDate(String championName) {
        String key = "champion:winRatePerDate:" + championName;

        return championWinRateCacheService.getOrLoad(
                key,
                () -> getChampionWinRate(championName),
                WIN_RATE_TTL
        );
    }

    private List<ChampionWinRatePerDateDto> getChampionWinRate(String championName) {
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

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<WinRateDto> getWinRateDaily() {
        LocalDate today = LocalDate.now(kst);
        String key = "winrate:daily:" + today;

        return winRateDailyCacheService.getOrLoad(
                key,
                () -> getWinRate(today, today),
                WIN_RATE_TTL
        );
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<WinRateDto> getWinRateWeekly() {
        LocalDate today     = LocalDate.now(kst);
        LocalDate weekStart = today.minusDays(6);
        String key = "winrate:weekly:" + weekStart + ":" + today;

        return winRateDailyCacheService.getOrLoad(
                key,
                () -> getWinRate(weekStart, today),
                WIN_RATE_TTL
        );
    }

    private List<WinRateDto> getWinRate(LocalDate date, LocalDate prev) {
        Instant startDate = date
                .atStartOfDay(kst)
                .toInstant();

        Instant prevDate = prev
                .atTime(LocalTime.MAX)
                .atZone(kst)
                .toInstant();

        long start = startDate.toEpochMilli();
        long end = prevDate.toEpochMilli();

        return participantRepository.findWinRateList(
                start,
                end,
                PageRequest.of(0, 10)
        );
    }
}
