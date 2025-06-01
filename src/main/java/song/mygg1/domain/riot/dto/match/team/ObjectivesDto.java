package song.mygg1.domain.riot.dto.match.team;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ObjectivesDto {
    private ObjectiveDto baron;
    private ObjectiveDto champion;
    private ObjectiveDto dragon;
    private ObjectiveDto horde;
    private ObjectiveDto inhibitor;
    private ObjectiveDto riftHerald;
    private ObjectiveDto tower;
}
