package song.mygg1.domain.riot.dto.champion;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ChampionLevelSkillStatsResponse {
    private Integer championId;
    private List<LevelSkillData> levelStats;
    private int totalAnalyzedPlayers;

    public ChampionLevelSkillStatsResponse(Integer championId, List<LevelSkillData> levelStats, int totalAnalyzedPlayers) {
        this.championId = championId;
        this.levelStats = levelStats;
        this.totalAnalyzedPlayers = totalAnalyzedPlayers;
    }
}
