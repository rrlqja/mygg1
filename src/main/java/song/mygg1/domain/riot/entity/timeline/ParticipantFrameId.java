package song.mygg1.domain.riot.entity.timeline;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantFrameId {
    @Embedded
    private FrameTimeLineId frameTimeLineId;
    private String participantId;
}
