package song.mygg1.domain.riot.dto.champion;

import java.util.HashMap;
import java.util.Map;

public class SkillSlotCounter {
    private Map<Integer, Integer> counts = new HashMap<>();

    public void incrementSkill(int skillSlot) {
        counts.put(skillSlot, counts.getOrDefault(skillSlot, 0) + 1);
    }

    public int getMostPickedSkillSlot() {
        if (counts.isEmpty()) {
            return 0;
        }
        return counts.entrySet().stream()
                .max((e1, e2) -> {
                    int countCompare = Integer.compare(e1.getValue(), e2.getValue());
                    if (countCompare == 0) {
                        return Integer.compare(e2.getKey(), e1.getKey());
                    }
                    return countCompare;
                })
                .map(Map.Entry::getKey)
                .orElse(0);
    }
}
