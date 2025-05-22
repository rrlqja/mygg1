package song.mygg1.domain.riot.mapper.timeline;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import song.mygg1.domain.riot.dto.timeline.ParticipantTimeLineDto;
import song.mygg1.domain.riot.entity.timeline.InfoTimeLine;
import song.mygg1.domain.riot.entity.timeline.ParticipantTimeLine;
import song.mygg1.domain.riot.entity.timeline.ParticipantTimelineId;

@Component
@RequiredArgsConstructor
public class ParticipantTimeLineMapper {
    public ParticipantTimeLineDto toDto(ParticipantTimeLine entity) {
        ParticipantTimeLineDto dto = new ParticipantTimeLineDto();

        dto.setPuuid(entity.getPuuid());
        dto.setParticipantId(entity.getId().getParticipantId());

        return dto;
    }

    public ParticipantTimeLine toEntity(ParticipantTimeLineDto dto, InfoTimeLine info) {
        ParticipantTimeLine entity = new ParticipantTimeLine();

        entity.setId(new ParticipantTimelineId(info.getMatchId(), dto.getParticipantId()));
        entity.setPuuid(dto.getPuuid());

        entity.setInfo(info);

        return entity;
    }
}
