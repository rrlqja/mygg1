package song.mygg1.domain.riot.dto.timeline.event;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import song.mygg1.domain.riot.dto.timeline.EventsTimeLineDto;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("ITEM_SOLD")
public class ItemSoldEventDto extends EventsTimeLineDto {
    private Integer itemId;
    private Integer participantId;
}
