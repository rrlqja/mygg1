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
@JsonTypeName("FEAT_UPDATE")
public class FeatUpdateEventDto extends EventsTimeLineDto {
    private Integer featType;
    private Integer featValue;
    private Integer teamId;
}
