package song.mygg1.domain.riot.dto.match.team;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamDto {
    private List<BanDto> bans = new ArrayList<>();
    private ObjectivesDto objectives;
    private Integer teamId;
    private boolean win;
}
