package song.mygg1.domain.riot.dto.match.team;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ObjectiveDto {
    private boolean first;
    private Integer kills;
}
