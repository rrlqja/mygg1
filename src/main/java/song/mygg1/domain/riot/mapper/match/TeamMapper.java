package song.mygg1.domain.riot.mapper.match;

import org.springframework.stereotype.Component;
import song.mygg1.domain.riot.dto.match.TeamDto;
import song.mygg1.domain.riot.entity.match.Info;
import song.mygg1.domain.riot.entity.match.Team;

@Component
public class TeamMapper {
    public TeamDto toDto(Team team) {
        TeamDto dto = new TeamDto();

        dto.setTeamId(team.getId().getTeamId());
        dto.setWin(team.isWin());

        return dto;
    }

    public Team toEntity(TeamDto dto, Info info) {
        return Team.create(dto.getTeamId(), dto.isWin(), info);
    }
}
