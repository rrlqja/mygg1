package song.mygg1.domain.riot.entity.timeline;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetadataTimeLine {
    @Id
    private String matchId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "match_id")
    private Timeline timeline;

    private String dataVersion;

    @ElementCollection
    @CollectionTable(
            name = "timeline_participants",
            joinColumns = @JoinColumn(name = "match_id")
    )
    private List<String> participants;
}
