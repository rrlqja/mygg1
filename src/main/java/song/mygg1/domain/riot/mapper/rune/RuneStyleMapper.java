package song.mygg1.domain.riot.mapper.rune;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import song.mygg1.domain.riot.dto.rune.RuneSlotDto;
import song.mygg1.domain.riot.dto.rune.RuneStyleDto;
import song.mygg1.domain.riot.entity.rune.RuneStyle;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RuneStyleMapper {
    private final RuneSlotMapper runeSlotMapper;

    public RuneStyleDto toDto(RuneStyle entity) {
        RuneStyleDto dto = new RuneStyleDto();

        dto.setId(entity.getId());
        dto.setKey(entity.getKey());
        dto.setName(entity.getName());
        dto.setIcon(entity.getIcon());

        List<RuneSlotDto> slots = entity.getSlots().stream()
                .map(runeSlotMapper::toDto)
                .toList();
        dto.setSlots(slots);

        return dto;
    }

    public RuneStyle toEntity(RuneStyleDto dto) {
        RuneStyle entity = new RuneStyle();

        entity.setId(dto.getId());
        entity.setKey(dto.getKey());
        entity.setName(dto.getName());
        entity.setIcon(dto.getIcon());

        dto.getSlots().forEach(rs -> runeSlotMapper.toEntity(rs, entity));

        return entity;
    }
}
