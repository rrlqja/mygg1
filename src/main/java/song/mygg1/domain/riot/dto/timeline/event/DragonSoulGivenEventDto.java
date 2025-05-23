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
@JsonTypeName("DRAGON_SOUL_GIVEN")
public class DragonSoulGivenEventDto extends EventsTimeLineDto {
    private String name;
    private Integer teamId;
}
