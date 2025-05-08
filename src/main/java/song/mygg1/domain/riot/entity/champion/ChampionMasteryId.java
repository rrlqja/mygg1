package song.mygg1.domain.riot.entity.champion;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChampionMasteryId implements Serializable {
    private String puuid;
    private Long championId;
}
