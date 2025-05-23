package song.mygg1.domain.riot.dto.timeline.event;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import song.mygg1.domain.riot.dto.timeline.EventsTimeLineDto;
import song.mygg1.domain.riot.dto.timeline.PositionDto;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("CHAMPION_SPECIAL_KILL")
public class ChampionSpecialKillEventDto extends EventsTimeLineDto {
    private String killType;
    private String killId;
    private PositionDto position;
}
