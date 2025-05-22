package song.mygg1.domain.riot.dto.timeline;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import song.mygg1.domain.riot.entity.timeline.DamageStats;

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

    public DamageStatsDto(DamageStats damageStats) {
        this.magicDamageDone = damageStats.getMagicDamageDone();
        this.magicDamageDoneToChampions = damageStats.getMagicDamageDoneToChampions();
        this.magicDamageTaken = damageStats.getMagicDamageTaken();
        this.physicalDamageDone = damageStats.getPhysicalDamageDone();
        this.physicalDamageTaken = damageStats.getPhysicalDamageTaken();
        this.totalDamageDone = damageStats.getTotalDamageDone();
        this.totalDamageDoneToChampions = damageStats.getTotalDamageDoneToChampions();
        this.totalDamageTaken = damageStats.getTotalDamageTaken();
        this.trueDamageDone = damageStats.getTrueDamageDone();
        this.trueDamageDoneToChampions = damageStats.getTrueDamageDoneToChampions();
        this.trueDamageTaken = damageStats.getTrueDamageTaken();
    }
}
