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
@DiscriminatorValue("TURRET_PLATE_DESTROYED")
public class TurretPlateDestroyEvent extends EventTimeLine {
    private Integer killerId;
    private String laneType;
    @Embedded
    private Position position;
    private Integer teamId;
}
