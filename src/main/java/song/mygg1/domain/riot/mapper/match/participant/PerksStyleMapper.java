package song.mygg1.domain.riot.mapper.match.participant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import song.mygg1.domain.riot.dto.match.participant.PerkStyleDto;
import song.mygg1.domain.riot.dto.match.participant.PerkStyleSelectionDto;
import song.mygg1.domain.riot.entity.match.participant.PerkStyle;
import song.mygg1.domain.riot.entity.match.participant.Perks;
import song.mygg1.domain.riot.entity.match.participant.PerkStyleSelection;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PerksStyleMapper {
    private final PerkStyleSelectionMapper perkStyleSelectionMapper;

    public PerkStyleDto toDto(PerkStyle entity) {
        PerkStyleDto dto = new PerkStyleDto();

        dto.setDescription(entity.getDescription());
        dto.setStyle(entity.getStyle());

        List<PerkStyleSelectionDto> selections = entity.getSelections().stream()
                .map(perkStyleSelectionMapper::toDto)
                .toList();
        dto.setSelections(selections);

        return dto;
    }

    public PerkStyle toEntity(PerkStyleDto dto) {
        PerkStyle entity = new PerkStyle();

        entity.setDescription(dto.getDescription());
        entity.setStyle(dto.getStyle());

        dto.getSelections().stream()
                .map(pssDto -> perkStyleSelectionMapper.toEntity(pssDto, entity))
                .forEach(entity::addSelection);

        return entity;
    }
}
