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
@DiscriminatorValue("CHAMPION_SPECIAL_KILL")
public class ChampionSpecialKillEvent extends EventTimeLine {
    private String killType;
    private String killId;
    @Embedded
    private Position position;
}
