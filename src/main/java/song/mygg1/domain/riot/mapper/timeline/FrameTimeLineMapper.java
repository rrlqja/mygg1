package song.mygg1.domain.riot.mapper.timeline;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import song.mygg1.domain.riot.dto.timeline.EventsTimeLineDto;
import song.mygg1.domain.riot.dto.timeline.FramesTimeLineDto;
import song.mygg1.domain.riot.dto.timeline.ParticipantFramesDto;
import song.mygg1.domain.riot.entity.timeline.EventTimeLine;
import song.mygg1.domain.riot.entity.timeline.FrameTimeLine;
import song.mygg1.domain.riot.entity.timeline.FrameTimeLineId;
import song.mygg1.domain.riot.entity.timeline.InfoTimeLine;
import song.mygg1.domain.riot.entity.timeline.ParticipantFrame;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FrameTimeLineMapper {
    private final EventTimeLineMapper eventTimeLineMapper;
    private final ParticipantFramesMapper participantFramesMapper;

    public FramesTimeLineDto toDto(FrameTimeLine entity) {
        FramesTimeLineDto dto = new FramesTimeLineDto();

        dto.setTimestamp(entity.getId().getTimestamp());

        List<EventsTimeLineDto> events = entity.getEvents().stream()
                .map(eventTimeLineMapper::toDto)
                .toList();
        dto.setEvents(events);

        ParticipantFramesDto frames = participantFramesMapper.toDto(entity.getParticipantFrames());
        dto.setParticipantFrames(frames);

        return dto;
    }

    public FrameTimeLine toEntity(FramesTimeLineDto dto, InfoTimeLine info) {
        FrameTimeLine entity = new FrameTimeLine();

        entity.setId(new FrameTimeLineId(info.getMatchId(), dto.getTimestamp()));

        List<EventTimeLine> events = dto.getEvents().stream()
                .map(e -> eventTimeLineMapper.toEntity(e, entity))
                .toList();
        entity.setEvents(events);

        List<ParticipantFrame> participants = participantFramesMapper.toEntity(dto.getParticipantFrames(), entity);
        entity.setParticipantFrames(participants);

        entity.setInfo(info);

        return entity;
    }
}
