package song.mygg1.domain.riot.entity.timeline.events;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import song.mygg1.domain.riot.entity.timeline.EventTimeLine;

@Entity
@Getter @Setter
@NoArgsConstructor
@DiscriminatorValue("SKILL_LEVEL_UP")
public class SkillLevelUpEvent extends EventTimeLine {
    private Integer skillSlot;
    private Integer participantId;
    private String levelUpType;
}
