package song.mygg1.domain.riot.entity.match.team;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Objective {
    private boolean first;
    private Integer kills;
}
