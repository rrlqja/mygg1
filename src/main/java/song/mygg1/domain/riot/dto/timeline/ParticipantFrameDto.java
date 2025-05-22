package song.mygg1.domain.riot.dto.timeline;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantFrameDto {
    private String participantId;
    private ChampionStatsDto championStats;
    private Integer currentGold;
    private DamageStatsDto damageStats;
    private Integer goldPerSecond;
    private Integer jungleMinionsKilled;
    private Integer level;
    private Integer minionsKilled;
    private PositionDto position;
    private Integer timeEnemySpentControlled;
    private Integer totalGold;
    private Integer xp;
}
