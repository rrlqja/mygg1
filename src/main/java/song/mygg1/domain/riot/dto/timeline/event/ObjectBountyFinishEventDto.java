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
@JsonTypeName("OBJECTIVE_BOUNTY_FINISH")
public class ObjectBountyFinishEventDto extends EventsTimeLineDto {
    private Integer teamId;
}
