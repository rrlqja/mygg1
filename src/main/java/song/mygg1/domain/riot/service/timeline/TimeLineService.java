package song.mygg1.domain.riot.service.timeline;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import song.mygg1.domain.redis.service.CacheService;
import song.mygg1.domain.riot.dto.timeline.TimelineDto;
import song.mygg1.domain.riot.entity.match.Matches;
import song.mygg1.domain.riot.entity.timeline.Timeline;
import song.mygg1.domain.riot.mapper.timeline.TimeLineMapper;
import song.mygg1.domain.riot.repository.match.MatchJpaRepository;
import song.mygg1.domain.riot.repository.timeline.TimeLineJpaRepository;
import song.mygg1.domain.riot.service.ApiService;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TimeLineService {
    private final CacheService<TimelineDto> cacheService;
    private final TimeLineJpaRepository timeLineRepository;
    private final MatchJpaRepository matchRepository;
    private final ApiService apiService;
    private final TimeLineMapper mapper;

    private static final Duration MATCH_TIMELINE_TTL = Duration.ofMinutes(10);

    @Scheduled(cron = "0 0 3 * * ?")
    @Transactional
    public void fetchRecentMatchTimelines() {
        log.info("Starting scheduled job to fetch recent match timelines.");

        LocalDate yesterday = LocalDate.now(ZoneId.of("Asia/Seoul")).minusDays(1);
        LocalDateTime startOfDay = yesterday.atStartOfDay();
        LocalDateTime endOfDay = yesterday.atTime(LocalTime.MAX);

        long startTimestamp = startOfDay.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();
        long endTimestamp = endOfDay.atZone(ZoneId.of("Asia/Seoul")).toInstant().toEpochMilli();

        Pageable pageable = PageRequest.of(0, 50);
        Page<Matches> matchesInPeriodPage;
        int processedCount = 0;
        int savedCount = 0;
        int skippedCount = 0;

        do {
            matchesInPeriodPage = matchRepository.findMatchesByGameCreation(startTimestamp, endTimestamp, pageable);

            if (matchesInPeriodPage.isEmpty()) {
                break;
            }

            for (Matches match : matchesInPeriodPage.getContent()) {
                String matchId = match.getMatchId();
                processedCount++;
                try {
                    boolean newlySaved = saveMatchTimeline(matchId);

                    if (newlySaved) {
                        savedCount++;
                        log.info("Successfully fetched and saved timeline for matchId: {}", matchId);
                         match.setTimelineFetched(true);
                         matchRepository.save(match);
                    } else {
                        skippedCount++;
                        log.info("Timeline for matchId: {} already exists or could not be fetched.", matchId);
                    }

                } catch (Exception e) {
                    skippedCount++;
                    log.error("Failed to process timeline for matchId: {}. Error: {}", matchId, e.getMessage());
                }
            }
            pageable = matchesInPeriodPage.nextPageable();
        } while (matchesInPeriodPage.hasNext());

        log.info("Finished scheduled job for recent match timelines. Total processed: {}, Newly saved: {}, Skipped/Failed: {}",
                processedCount, savedCount, skippedCount);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean saveMatchTimeline(String matchId) {
        if (timeLineRepository.findTimelineByMatchId(matchId).isPresent()) {
            return false;
        }

        Optional<TimelineDto> timelineDtoOpt = apiService.getMatchTimeline(matchId);

        if (timelineDtoOpt.isPresent()) {
            TimelineDto riotTimelineDto = timelineDtoOpt.get();
            try {
                Timeline entity = mapper.toEntity(riotTimelineDto);
                timeLineRepository.save(entity);

                log.info("Successfully fetched and saved timeline for matchId: {}", matchId);
                return true;
            } catch (Exception e) {
                log.error("Error saving timeline data for matchId: {} after fetching from API. Error: {}", matchId, e.getMessage(), e);
                return false;
            }
        } else {
            log.warn("Could not fetch timeline from API for matchId: {}. (API returned empty or error)", matchId);
            return false;
        }
    }
}
