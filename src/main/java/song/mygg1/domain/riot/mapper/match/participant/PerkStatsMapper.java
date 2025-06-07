package song.mygg1.domain.riot.mapper.match.participant;

import org.springframework.stereotype.Component;
import song.mygg1.domain.riot.dto.match.participant.PerkStatsDto;
import song.mygg1.domain.riot.entity.match.participant.PerkStats;

@Component
public class PerkStatsMapper {
    public PerkStatsDto toDto(PerkStats entity) {
        PerkStatsDto dto = new PerkStatsDto();

        dto.setDefense(entity.getDefense());
        dto.setFlex(entity.getFlex());
        dto.setOffense(entity.getOffense());

        return dto;
    }

    public PerkStats toEntity(PerkStatsDto dto) {
        PerkStats entity = new PerkStats();

        entity.setDefense(dto.getDefense());
        entity.setFlex(dto.getFlex());
        entity.setOffense(dto.getOffense());

        return entity;
    }
}
