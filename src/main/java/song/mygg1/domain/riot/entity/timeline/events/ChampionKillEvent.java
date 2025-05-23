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
import song.mygg1.domain.riot.entity.timeline.DamageInfo;
import song.mygg1.domain.riot.entity.timeline.EventTimeLine;
import song.mygg1.domain.riot.entity.timeline.Position;

import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@DiscriminatorValue("CHAMPION_KILL")
public class ChampionKillEvent extends EventTimeLine {
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "champion_kill_assists",
            joinColumns = @JoinColumn(name = "event_id")
    )
    private List<Integer> assistingParticipantIds;
    private Integer bounty;
    private Integer killStreakLength;
    private Integer killerId;
    @Embedded
    private Position position;
    private Integer shutdownBounty;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "champion_kill_damage_dealt",
            joinColumns = @JoinColumn(name = "event_id")
    )
    private List<DamageInfo> victimDamageDealt;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "champion_kill_damage_received",
            joinColumns = @JoinColumn(name = "event_id")
    )
    private List<DamageInfo> victimDamageReceived;
    private Integer victimId;
}
