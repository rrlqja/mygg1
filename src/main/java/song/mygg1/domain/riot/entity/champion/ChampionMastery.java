package song.mygg1.domain.riot.entity.champion;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChampionMastery {
    @EmbeddedId
    private ChampionMasteryId championMasteryId;

    private Integer championLevel;
    private Integer championPoints;

    public static ChampionMastery create(String puuid, Long championId, Integer championLevel, Integer championPoints) {
        return new ChampionMastery(puuid, championId, championLevel, championPoints);
    }

    private ChampionMastery(String puuid, Long championId, Integer championLevel, Integer championPoints) {
        this.championMasteryId = new ChampionMasteryId(puuid, championId);
        this.championLevel = championLevel;
        this.championPoints = championPoints;
    }
}
