package song.mygg1.domain.riot.entity.timeline.events;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import song.mygg1.domain.riot.entity.timeline.EventTimeLine;

@ToString
@Entity
@Getter @Setter
@NoArgsConstructor
@DiscriminatorValue("ITEM_PURCHASED")
public class ItemPurchaseEvent extends EventTimeLine {
    private Integer itemId;
    private Integer participantId;
}
