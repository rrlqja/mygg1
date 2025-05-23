package song.mygg1.domain.riot.entity.timeline.events;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import song.mygg1.domain.riot.entity.timeline.EventTimeLine;
import song.mygg1.domain.riot.entity.timeline.Position;

@Entity
@Getter @Setter
@NoArgsConstructor
@DiscriminatorValue("ELITE_MONSTER_KILL")
public class EliteMonsterKillEvent extends EventTimeLine {
    private Integer bounty;
    private Integer killerId;
    private Integer killerTeamId;
    private String monsterSubType;
    private String monsterType;
    @Embedded
    private Position position;
}
