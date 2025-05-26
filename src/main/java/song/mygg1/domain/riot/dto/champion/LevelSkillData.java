package song.mygg1.domain.riot.dto.champion;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class LevelSkillData {
    private int level;
    private Map<String, Double> skillPickPercentages;
    private String mostPickedSkill;
    private int totalPicksAtThisLevel;

    public LevelSkillData(int level, Map<String, Double> skillPickPercentages, String mostPickedSkill, int totalPicksAtThisLevel) {
        this.level = level;
        this.skillPickPercentages = skillPickPercentages;
        this.mostPickedSkill = mostPickedSkill;
        this.totalPicksAtThisLevel = totalPicksAtThisLevel;
    }
}
