package song.mygg1.domain.riot.mapper.champion;

import org.springframework.stereotype.Component;
import song.mygg1.domain.riot.dto.champion.ChampionMasteryDto;
import song.mygg1.domain.riot.entity.champion.ChampionMastery;

@Component
public class ChampionMasterMapper {
    public ChampionMasteryDto toDto(ChampionMastery championMastery) {
        ChampionMasteryDto dto = new ChampionMasteryDto();

        dto.setPuuid(championMastery.getChampionMasteryId().getPuuid());
        dto.setChampionId(championMastery.getChampionMasteryId().getChampionId());
        dto.setChampionLevel(championMastery.getChampionLevel());
        dto.setChampionPoints(championMastery.getChampionPoints());

        return dto;
    }

    public ChampionMastery toEntity(ChampionMasteryDto dto) {
        return ChampionMastery.create(
                dto.getPuuid(),
                dto.getChampionId(),
                dto.getChampionLevel(),
                dto.getChampionPoints()
        );
    }
}
