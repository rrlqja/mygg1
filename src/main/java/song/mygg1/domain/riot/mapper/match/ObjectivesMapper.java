package song.mygg1.domain.riot.mapper.match;

import org.springframework.stereotype.Component;
import song.mygg1.domain.riot.dto.match.team.ObjectiveDto;
import song.mygg1.domain.riot.dto.match.team.ObjectivesDto;
import song.mygg1.domain.riot.entity.match.team.Objective;
import song.mygg1.domain.riot.entity.match.team.Objectives;

@Component
public class ObjectivesMapper {
    public ObjectivesDto toDto(Objectives entity) {
        ObjectivesDto dto = new ObjectivesDto();

        dto.setBaron(new ObjectiveDto(entity.getBaron().isFirst(), entity.getBaron().getKills()));
        dto.setChampion(new ObjectiveDto(entity.getChampion().isFirst(), entity.getChampion().getKills()));
        dto.setDragon(new ObjectiveDto(entity.getDragon().isFirst(), entity.getDragon().getKills()));
        dto.setHorde(new ObjectiveDto(entity.getHorde().isFirst(), entity.getHorde().getKills()));
        dto.setInhibitor(new ObjectiveDto(entity.getInhibitor().isFirst(), entity.getInhibitor().getKills()));
        dto.setRiftHerald(new ObjectiveDto(entity.getRiftHerald().isFirst(), entity.getRiftHerald().getKills()));
        dto.setTower(new ObjectiveDto(entity.getTower().isFirst(), entity.getTower().getKills()));

        return dto;
    }

    public Objectives toEntity(ObjectivesDto dto) {
        Objectives entity = new Objectives();

        entity.setBaron(new Objective(dto.getBaron().isFirst(), dto.getBaron().getKills()));
        entity.setChampion(new Objective(dto.getChampion().isFirst(), dto.getChampion().getKills()));
        entity.setDragon(new Objective(dto.getDragon().isFirst(), dto.getDragon().getKills()));
        entity.setHorde(new Objective(dto.getHorde().isFirst(), dto.getHorde().getKills()));
        entity.setInhibitor(new Objective(dto.getInhibitor().isFirst(), dto.getInhibitor().getKills()));
        entity.setRiftHerald(new Objective(dto.getRiftHerald().isFirst(), dto.getRiftHerald().getKills()));
        entity.setTower(new Objective(dto.getTower().isFirst(), dto.getTower().getKills()));

        return entity;
    }
}
