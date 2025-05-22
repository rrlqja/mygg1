package song.mygg1.domain.riot.entity.timeline;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import song.mygg1.domain.riot.dto.timeline.DamageStatsDto;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class DamageStats {
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

    public DamageStats(DamageStatsDto dto) {
        this.magicDamageDone = dto.getMagicDamageDone();
        this.magicDamageDoneToChampions = dto.getMagicDamageDoneToChampions();
        this.magicDamageTaken = dto.getMagicDamageTaken();
        this.physicalDamageDone = dto.getPhysicalDamageDone();
        this.physicalDamageDoneToChampions = dto.getPhysicalDamageDoneToChampions();
        this.physicalDamageTaken = dto.getPhysicalDamageTaken();
        this.totalDamageDone = dto.getTotalDamageDone();
        this.totalDamageDoneToChampions = dto.getTotalDamageDoneToChampions();
        this.totalDamageTaken = dto.getTotalDamageTaken();
        this.trueDamageDone = dto.getTrueDamageDone();
        this.trueDamageDoneToChampions = dto.getTrueDamageDoneToChampions();
        this.trueDamageTaken = dto.getTrueDamageTaken();
    }
}
