package song.mygg1.domain.riot.mapper.timeline;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import song.mygg1.domain.riot.dto.timeline.ParticipantFrameDto;
import song.mygg1.domain.riot.dto.timeline.ParticipantFramesDto;
import song.mygg1.domain.riot.entity.timeline.FrameTimeLine;
import song.mygg1.domain.riot.entity.timeline.ParticipantFrame;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ParticipantFramesMapper {
    private final ParticipantFrameMapper participantFrameMapper;

    public ParticipantFramesDto toDto(List<ParticipantFrame> entityList) {
        ParticipantFramesDto dto = new ParticipantFramesDto();

        List<ParticipantFrameDto> list = entityList.stream()
                .map(participantFrameMapper::toDto)
                .toList();

        list.forEach(entity -> {
            dto.setFrame(entity.getParticipantId(), entity);
        });

        return dto;
    }

    public List<ParticipantFrame> toEntity(ParticipantFramesDto dto, FrameTimeLine frame) {
        List<ParticipantFrame> entityList = new ArrayList<>();

        entityList = dto.getFrames().values().stream()
                .map(p -> participantFrameMapper.toEntity(p, frame))
                .toList();

        return entityList;
    }
}
