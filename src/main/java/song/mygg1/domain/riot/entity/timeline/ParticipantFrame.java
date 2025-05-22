package song.mygg1.domain.riot.entity.timeline;

import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantFrame {
    @EmbeddedId
    private ParticipantFrameId id;

    @ManyToOne
    @MapsId("frameTimeLineId")
    @JoinColumns({
            @JoinColumn(name = "match_id", referencedColumnName = "match_id"),
            @JoinColumn(name = "frame_timestamp", referencedColumnName = "timestamp")
    })
    private FrameTimeLine frame;

    private Integer currentGold;
    private Integer goldPerSecond;
    private Integer jungleMinionsKilled;
    private Integer level;
    private Integer minionsKilled;
    private Integer timeEnemySpentControlled;
    private Integer totalGold;
    private Integer xp;

    @Embedded
    private ChampionStats championStats;
    @Embedded
    private DamageStats damageStats;
    @Embedded
    private Position position;
}
