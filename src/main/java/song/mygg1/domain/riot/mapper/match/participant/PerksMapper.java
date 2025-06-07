package song.mygg1.domain.riot.mapper.match.participant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import song.mygg1.domain.riot.dto.match.participant.PerkStatsDto;
import song.mygg1.domain.riot.dto.match.participant.PerkStyleDto;
import song.mygg1.domain.riot.dto.match.participant.PerksDto;
import song.mygg1.domain.riot.entity.match.Participant;
import song.mygg1.domain.riot.entity.match.participant.PerkStats;
import song.mygg1.domain.riot.entity.match.participant.PerkStyle;
import song.mygg1.domain.riot.entity.match.participant.Perks;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PerksMapper {
    private final PerkStatsMapper perkStatsMapper;
    private final PerksStyleMapper perksStyleMapper;

    public PerksDto toDto(Perks entity) {
        PerksDto dto = new PerksDto();

        PerkStatsDto perkStatDto = perkStatsMapper.toDto(entity.getStatPerks());
        dto.setStatPerks(perkStatDto);

        List<PerkStyleDto> styles = entity.getStyles().stream()
                .map(perksStyleMapper::toDto)
                .toList();
        dto.setStyles(styles);

        return dto;
    }

    public Perks toEntity(PerksDto dto) {
        Perks entity = new Perks();

        PerkStats perkStats = perkStatsMapper.toEntity(dto.getStatPerks());
        entity.setStatPerks(perkStats);

        dto.getStyles().stream()
                .map(perksStyleMapper::toEntity)
                .forEach(entity::addStyle);

        return entity;
    }
}
