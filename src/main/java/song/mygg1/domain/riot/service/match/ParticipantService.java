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
    private final CacheService<List<WinRateDto>> winRateCacheService;

    private static final ZoneId kst = ZoneId.of("Asia/Seoul");
    private static final Duration WIN_RATE_TTL = Duration.ofDays(1L);
    private static final Duration DAILY_WIN_RATE_TTL = Duration.ofDays(1L);

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
        LocalDate yesterday = LocalDate.now(kst).minusDays(2);
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
        LocalDate yesterday      = LocalDate.now(kst).minusDays(2);
        LocalDate dayBefore      = LocalDate.now(kst).minusDays(3);
        String key = "winrate:daily:" + yesterday;

        return winRateCacheService.getOrLoad(
                key,
                () -> getWinRate(yesterday, dayBefore),
                DAILY_WIN_RATE_TTL
        );
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<WinRateDto> getWinRateWeekly() {
        LocalDate yesterday      = LocalDate.now(kst).minusDays(2);
        LocalDate thisWeekStart  = yesterday.minusDays(6);
        LocalDate prevWeekEnd    = thisWeekStart.minusDays(1);
        LocalDate prevWeekStart  = prevWeekEnd.minusDays(6);
        String key = "winrate:weekly:"
                + thisWeekStart + ":" + yesterday
                + ":" + prevWeekStart + ":" + prevWeekEnd;

        return winRateCacheService.getOrLoad(
                key,
                () -> getWinRate(thisWeekStart, yesterday, prevWeekStart, prevWeekEnd),
                WIN_RATE_TTL
        );
    }

    private List<WinRateDto> getWinRate(LocalDate currentDate, LocalDate prevDate) {
        Instant currentStart = currentDate.atStartOfDay(kst).toInstant();
        Instant currentEnd   = currentDate.atTime(LocalTime.MAX).atZone(kst).toInstant();
        Instant prevStart    = prevDate.atStartOfDay(kst).toInstant();
        Instant prevEnd      = prevDate.atTime(LocalTime.MAX).atZone(kst).toInstant();

        return participantRepository.findWinRateList(
                currentStart.toEpochMilli(),
                currentEnd.toEpochMilli(),
                prevStart.toEpochMilli(),
                prevEnd.toEpochMilli(),
                PageRequest.of(0, 10)
        );
    }

    private List<WinRateDto> getWinRate(
            LocalDate curStart, LocalDate curEnd,
            LocalDate prevStart, LocalDate prevEnd) {
        Instant cs = curStart.atStartOfDay(kst).toInstant();
        Instant ce = curEnd  .atTime(LocalTime.MAX).atZone(kst).toInstant();
        Instant ps = prevStart.atStartOfDay(kst).toInstant();
        Instant pe = prevEnd  .atTime(LocalTime.MAX).atZone(kst).toInstant();

        return participantRepository.findWinRateList(
                cs.toEpochMilli(),
                ce.toEpochMilli(),
                ps.toEpochMilli(),
                pe.toEpochMilli(),
                PageRequest.of(0, 10)
        );
    }
}
