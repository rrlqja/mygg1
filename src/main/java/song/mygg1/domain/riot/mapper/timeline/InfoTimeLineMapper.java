package song.mygg1.domain.riot.mapper.timeline;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import song.mygg1.domain.riot.dto.timeline.FramesTimeLineDto;
import song.mygg1.domain.riot.dto.timeline.InfoTimeLineDto;
import song.mygg1.domain.riot.dto.timeline.ParticipantTimeLineDto;
import song.mygg1.domain.riot.entity.timeline.FrameTimeLine;
import song.mygg1.domain.riot.entity.timeline.InfoTimeLine;
import song.mygg1.domain.riot.entity.timeline.ParticipantTimeLine;
import song.mygg1.domain.riot.entity.timeline.Timeline;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InfoTimeLineMapper {
    private final ParticipantTimeLineMapper participantTimeLineMapper;
    private final FrameTimeLineMapper frameTimeLineMapper;

    public InfoTimeLineDto toDto(InfoTimeLine entity) {
        InfoTimeLineDto dto = new InfoTimeLineDto();

        dto.setGameId(entity.getGameId());
        dto.setEndOfGameResult(entity.getEndOfGameResult());
        dto.setFrameInterval(entity.getFrameInterval());

        List<ParticipantTimeLineDto> participants = entity.getParticipants().stream()
                .map(participantTimeLineMapper::toDto)
                .toList();
        dto.setParticipants(participants);

        List<FramesTimeLineDto> frames = entity.getFrames().stream()
                .map(frameTimeLineMapper::toDto)
                .toList();
        dto.setFrames(frames);

        return dto;
    }

    public InfoTimeLine toEntity(InfoTimeLineDto dto, Timeline timeline) {
        InfoTimeLine entity = new InfoTimeLine();

        entity.setMatchId(timeline.getMatchId());

        entity.setEndOfGameResult(dto.getEndOfGameResult());
        entity.setFrameInterval(dto.getFrameInterval());
        entity.setGameId(dto.getGameId());

        List<ParticipantTimeLine> participants = dto.getParticipants().stream()
                .map(p -> participantTimeLineMapper.toEntity(p, entity))
                .toList();
        entity.setParticipants(participants);

        List<FrameTimeLine> frames = dto.getFrames().stream()
                .map(f -> frameTimeLineMapper.toEntity(f, entity))
                .toList();
        entity.setFrames(frames);

        entity.setTimeline(timeline);

        return entity;
    }
}
