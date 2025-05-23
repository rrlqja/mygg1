package song.mygg1.domain.riot.dto.timeline.event;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import song.mygg1.domain.riot.dto.timeline.EventsTimeLineDto;
import song.mygg1.domain.riot.dto.timeline.PositionDto;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("BUILDING_KILL")
public class BuildingKillEventDto extends EventsTimeLineDto {
    private List<Integer> assistingParticipantIds = new ArrayList<>();
    private Integer bounty;
    private String buildingType;
    private Integer killerId;
    private String laneType;
    private PositionDto position;
    private Integer teamId;
    private String towerType;
}
