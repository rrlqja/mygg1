package song.mygg1.domain.riot.mapper.match;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import song.mygg1.domain.riot.dto.match.team.TeamDto;
import song.mygg1.domain.riot.entity.match.Info;
import song.mygg1.domain.riot.entity.match.team.Ban;
import song.mygg1.domain.riot.entity.match.team.Objectives;
import song.mygg1.domain.riot.entity.match.team.Team;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TeamMapper {
    private final ObjectivesMapper objectivesMapper;
    private final BansMapper bansMapper;

    public TeamDto toDto(Team team) {
        TeamDto dto = new TeamDto();

        dto.setTeamId(team.getId().getTeamId());
        dto.setWin(team.isWin());

        return dto;
    }

    public Team toEntity(TeamDto dto, Info info) {
        Objectives objectives = objectivesMapper.toEntity(dto.getObjectives());

        Team entity = Team.create(dto.getTeamId(), dto.isWin(), info, objectives);

        List<Ban> bans = dto.getBans().stream()
                .map(b -> bansMapper.toEntity(b, entity))
                .toList();
        entity.setBans(bans);

        return entity;
    }

}
