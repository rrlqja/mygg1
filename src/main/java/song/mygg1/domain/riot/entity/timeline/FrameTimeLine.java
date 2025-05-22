package song.mygg1.domain.riot.entity.timeline;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FrameTimeLine {
    @EmbeddedId
    private FrameTimeLineId id;

    @ManyToOne
    @MapsId("matchId")
    @JoinColumn(name = "match_id")
    private InfoTimeLine info;

    @OneToMany(mappedBy = "frame", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventTimeLine> events = new ArrayList<>();

    @OneToMany(mappedBy = "frame", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParticipantFrame> participantFrames = new ArrayList<>();

}
