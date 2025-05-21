package song.mygg1.domain.riot.dto.timeline;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DamageStatsDto {
    private Integer magicDamageDone;
    private Integer magicDamageDoneToChampions;
    private Integer magicDamageTaken;
    private Integer physicalDamageDone;
    private Integer physicalDamageDoneToChampions;
    private Integer physicalDamageTaken;
    private Integer totalDamageDone;
    private Integer totalDamageDoneToChampions;
    private Integer totalDamageTaken;
    private Integer trueDamageDone;
    private Integer trueDamageDoneToChampions;
    private Integer trueDamageTaken;
}
