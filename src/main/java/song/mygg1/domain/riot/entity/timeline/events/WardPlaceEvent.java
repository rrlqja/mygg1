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
@DiscriminatorValue("WARD_PLACED")
public class WardPlaceEvent extends EventTimeLine {
    private Integer creatorId;
    private String wardType;
}
