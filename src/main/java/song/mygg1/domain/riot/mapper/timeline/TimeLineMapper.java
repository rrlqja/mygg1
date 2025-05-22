package song.mygg1.domain.riot.mapper.timeline;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import song.mygg1.domain.riot.dto.timeline.InfoTimeLineDto;
import song.mygg1.domain.riot.dto.timeline.MetadataTimeLineDto;
import song.mygg1.domain.riot.dto.timeline.TimelineDto;
import song.mygg1.domain.riot.entity.timeline.InfoTimeLine;
import song.mygg1.domain.riot.entity.timeline.MetadataTimeLine;
import song.mygg1.domain.riot.entity.timeline.Timeline;

@Component
@RequiredArgsConstructor
public class TimeLineMapper {
    private final MetadataTImeLineMapper metadataTImeLineMapper;
    private final InfoTimeLineMapper infoTimeLineMapper;

    public TimelineDto toDto(Timeline entity) {
        TimelineDto dto = new TimelineDto();

        MetadataTimeLineDto metadataDto = metadataTImeLineMapper.toDto(entity.getMetadata());
        dto.setMetadata(metadataDto);

        InfoTimeLineDto infoDto = infoTimeLineMapper.toDto(entity.getInfo());
        dto.setInfo(infoDto);

        return dto;
    }

    public Timeline toEntity(TimelineDto dto) {
        Timeline entity = new Timeline();

        entity.setMatchId(dto.getMetadata().getMatchId());

        MetadataTimeLine metadata = metadataTImeLineMapper.toEntity(dto.getMetadata(), entity);
        entity.setMetadata(metadata);

        InfoTimeLine info = infoTimeLineMapper.toEntity(dto.getInfo(), entity);
        entity.setInfo(info);

        return entity;
    }
}
