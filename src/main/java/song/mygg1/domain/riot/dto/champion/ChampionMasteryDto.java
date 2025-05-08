package song.mygg1.domain.riot.dto.champion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import song.mygg1.domain.riot.entity.champion.ChampionMastery;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChampionMasteryDto {
    private String puuid;
    private Long championId;
    private Integer championLevel;
    private Integer championPoints;

    public ChampionMastery toEntity() {
        return ChampionMastery.create(puuid, championId, championLevel, championPoints);
    }

    public ChampionMasteryDto(ChampionMastery championMastery) {
        this.puuid = championMastery.getChampionMasteryId().getPuuid();
        this.championId = championMastery.getChampionMasteryId().getChampionId();
        this.championLevel = championMastery.getChampionLevel();
        this.championPoints = championMastery.getChampionPoints();
    }
}
