package song.mygg1.domain.riot.mapper.timeline;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import song.mygg1.domain.riot.dto.timeline.EventsTimeLineDto;
import song.mygg1.domain.riot.entity.timeline.EventTimeLine;
import song.mygg1.domain.riot.entity.timeline.FrameTimeLine;

@Component
@RequiredArgsConstructor
public class EventTimeLineMapper {
    public EventsTimeLineDto toDto(EventTimeLine entity) {
        EventsTimeLineDto dto = new EventsTimeLineDto();

        dto.setTimestamp(entity.getTimestamp());
        dto.setRealTimestamp(entity.getRealTimestamp());
        dto.setType(entity.getType());

        return dto;
    }

    public EventTimeLine toEntity(EventsTimeLineDto dto, FrameTimeLine frame) {
        EventTimeLine entity = new EventTimeLine();

        entity.setTimestamp(dto.getTimestamp());
        entity.setRealTimestamp(dto.getRealTimestamp());
        entity.setType(dto.getType());

        entity.setFrame(frame);

        return entity;
    }
}
