package song.mygg1.domain.riot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import song.mygg1.domain.riot.entity.Info;
import song.mygg1.domain.riot.entity.Team;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamDto {
    private Integer teamId;
    private boolean win;

    public Team toEntity(Info info) {
        return Team.create(teamId, win, info);
    }

    public TeamDto(Team team) {
        this.teamId = team.getTeamId();
        this.win = team.isWin();
    }
}
