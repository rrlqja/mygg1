package song.mygg1.domain.riot.entity.timeline.events;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import song.mygg1.domain.riot.entity.timeline.EventTimeLine;
import song.mygg1.domain.riot.entity.timeline.Position;

import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@DiscriminatorValue("BUILDING_KILL")
public class BuildingKillEvent extends EventTimeLine {
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "building_kill_assists",
            joinColumns = @JoinColumn(name = "event_id")
    )
    private List<Integer> assistingParticipantIds;
    private Integer bounty;
    private String buildingType;
    private Integer killerId;
    private String laneType;
    @Embedded
    private Position position;
    private Integer teamId;
    private String towerType;
}
