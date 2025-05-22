package song.mygg1.domain.riot.entity.timeline;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantTimelineId {
    private String matchId;
    private Integer participantId;
}
