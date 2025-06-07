package song.mygg1.domain.riot.mapper.match.participant;

import org.springframework.stereotype.Component;
import song.mygg1.domain.riot.dto.match.participant.PerkStyleSelectionDto;
import song.mygg1.domain.riot.entity.match.participant.PerkStyle;
import song.mygg1.domain.riot.entity.match.participant.PerkStyleSelection;

@Component
public class PerkStyleSelectionMapper {
    public PerkStyleSelectionDto toDto(PerkStyleSelection entity) {
        PerkStyleSelectionDto dto = new PerkStyleSelectionDto();

        dto.setPerk(entity.getPerk());
        dto.setVar1(entity.getVar1());
        dto.setVar2(entity.getVar2());
        dto.setVar3(entity.getVar3());

        return dto;
    }

    public PerkStyleSelection toEntity(PerkStyleSelectionDto dto, PerkStyle perkStyle) {
        PerkStyleSelection entity = new PerkStyleSelection();

        entity.setPerk(dto.getPerk());
        entity.setVar1(dto.getVar1());
        entity.setVar2(dto.getVar2());
        entity.setVar3(dto.getVar3());

        perkStyle.addSelection(entity);

        return entity;
    }
}
