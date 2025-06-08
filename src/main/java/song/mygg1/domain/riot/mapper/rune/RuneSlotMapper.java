package song.mygg1.domain.riot.mapper.rune;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import song.mygg1.domain.riot.dto.rune.RuneDto;
import song.mygg1.domain.riot.dto.rune.RuneSlotDto;
import song.mygg1.domain.riot.entity.rune.RuneSlot;
import song.mygg1.domain.riot.entity.rune.RuneStyle;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RuneSlotMapper {
    private final RuneMapper runeMapper;

    public RuneSlotDto toDto(RuneSlot entity) {
        RuneSlotDto dto = new RuneSlotDto();

        List<RuneDto> runes = entity.getRunes().stream()
                .map(runeMapper::toDto)
                .toList();
        dto.setRunes(runes);

        return dto;
    }

    public RuneSlot toEntity(RuneSlotDto dto, RuneStyle runeStyle) {
        RuneSlot entity = new RuneSlot();

        dto.getRunes().forEach(r -> runeMapper.toEntity(r, entity));

        runeStyle.addRuneSlot(entity);

        return entity;
    }
}
