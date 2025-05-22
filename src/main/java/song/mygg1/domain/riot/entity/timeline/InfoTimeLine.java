package song.mygg1.domain.riot.entity.timeline;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InfoTimeLine {
    @Id
    private String matchId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "match_id")
    private Timeline timeline;

    private String endOfGameResult;
    private Long frameInterval;
    private Long gameId;

    @OneToMany(mappedBy = "info", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParticipantTimeLine> participants;

    @OneToMany(mappedBy = "info", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FrameTimeLine> frames;
}
