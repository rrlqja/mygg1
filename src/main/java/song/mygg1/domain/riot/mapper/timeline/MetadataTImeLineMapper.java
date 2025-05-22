package song.mygg1.domain.riot.mapper.timeline;

import org.springframework.stereotype.Component;
import song.mygg1.domain.riot.dto.timeline.MetadataTimeLineDto;
import song.mygg1.domain.riot.entity.timeline.MetadataTimeLine;
import song.mygg1.domain.riot.entity.timeline.Timeline;

@Component
public class MetadataTImeLineMapper {
    public MetadataTimeLineDto toDto(MetadataTimeLine entity) {
        MetadataTimeLineDto dto = new MetadataTimeLineDto();

        dto.setDataVersion(entity.getDataVersion());
        dto.setMatchId(entity.getMatchId());
        dto.setParticipants(entity.getParticipants());

        return dto;
    }

    public MetadataTimeLine toEntity(MetadataTimeLineDto metadata, Timeline timeline) {
        MetadataTimeLine entity = new MetadataTimeLine();

        entity.setMatchId(metadata.getMatchId());
        entity.setDataVersion(metadata.getDataVersion());
        entity.setParticipants(metadata.getParticipants());

        entity.setTimeline(timeline);

        return entity;
    }
}
