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
@JsonTypeName("TURRET_PLATE_DESTROYED")
public class TurretPlateDestroyEventDto extends EventsTimeLineDto {
    private Integer killerId;
    private String laneType;
    private PositionDto position;
    private Integer teamId;
}
