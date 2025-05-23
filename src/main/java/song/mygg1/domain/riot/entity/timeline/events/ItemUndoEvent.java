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
@DiscriminatorValue("ITEM_UNDO")
public class ItemUndoEvent extends EventTimeLine {
    private Integer afterId;
    private Integer beforeId;
    private Integer goldGain;
    private Integer participantId;
}
