package song.mygg1.domain.riot.mapper.rune;

import org.springframework.stereotype.Component;
import song.mygg1.domain.riot.dto.rune.RuneDto;
import song.mygg1.domain.riot.entity.rune.Rune;
import song.mygg1.domain.riot.entity.rune.RuneSlot;

@Component
public class RuneMapper {
    public RuneDto toDto(Rune entity) {
        RuneDto dto = new RuneDto();

        dto.setId(entity.getId());
        dto.setKey(entity.getKey());
        dto.setName(entity.getName());
        dto.setIcon(entity.getIcon());
        dto.setShortDesc(entity.getShortDesc());
        dto.setLongDesc(entity.getLongDesc());

        return dto;
    }

    public Rune toEntity(RuneDto dto, RuneSlot runeSlot) {
        Rune entity = new Rune();

        entity.setId(dto.getId());
        entity.setKey(dto.getKey());
        entity.setName(dto.getName());
        entity.setIcon(dto.getIcon());
        entity.setShortDesc(dto.getShortDesc());
        entity.setLongDesc(dto.getLongDesc());

        runeSlot.addRune(entity);

        return entity;
    }
}
