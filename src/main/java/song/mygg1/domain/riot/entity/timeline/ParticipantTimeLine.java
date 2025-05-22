package song.mygg1.domain.riot.entity.timeline;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantTimeLine {
    @EmbeddedId
    private ParticipantTimelineId id;

    @ManyToOne
    @MapsId("matchId")
    @JoinColumn(name = "match_id")
    private InfoTimeLine info;

    private String puuid;
}
