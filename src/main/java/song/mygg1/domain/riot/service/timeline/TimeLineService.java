package song.mygg1.domain.riot.service.timeline;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import song.mygg1.domain.common.exception.riot.match.exceptions.MatchNotFoundException;
import song.mygg1.domain.redis.service.CacheService;
import song.mygg1.domain.riot.dto.timeline.TimelineDto;
import song.mygg1.domain.riot.entity.timeline.Timeline;
import song.mygg1.domain.riot.mapper.timeline.TimeLineMapper;
import song.mygg1.domain.riot.repository.timeline.TimeLineJpaRepository;
import song.mygg1.domain.riot.service.ApiService;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class TimeLineService {
    private final CacheService<TimelineDto> cacheService;
    private final TimeLineJpaRepository timeLineRepository;
    private final ApiService apiService;
    private final TimeLineMapper mapper;

    private static final Duration MATCH_TIMELINE_TTL = Duration.ofMinutes(10);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public TimelineDto getTimeline(String matchId) {
        String key = "match:timeline:" + matchId;

        TimelineDto dto = cacheService.getOrLoad(
                key,
                () -> mapper.toDto(saveMatchTimeline(matchId)),
                MATCH_TIMELINE_TTL
        );

        return dto;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Timeline saveMatchTimeline(String matchId) {
        return timeLineRepository.findTimelineByMatchId(matchId)
                .orElseGet(() -> {
                    TimelineDto timelineDto = apiService.getMatchTimeline(matchId)
                            .orElseThrow(MatchNotFoundException::new);
                    log.info("get timeline: {}", timelineDto.getMetadata().getMatchId());
                    Timeline entity = mapper.toEntity(timelineDto);
                    return timeLineRepository.save(entity);
                });
    }
}
