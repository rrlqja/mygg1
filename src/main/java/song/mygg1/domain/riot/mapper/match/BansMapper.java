package song.mygg1.domain.riot.mapper.match;

import org.springframework.stereotype.Component;
import song.mygg1.domain.riot.dto.match.team.BanDto;
import song.mygg1.domain.riot.entity.match.team.Ban;
import song.mygg1.domain.riot.entity.match.team.Team;

@Component
public class BansMapper {
    public BanDto toDto(Ban ban) {
        BanDto dto = new BanDto();

        dto.setChampionId(ban.getChampionId());
        dto.setPickTurn(ban.getPickTurn());

        return dto;
    }

    public Ban toEntity(BanDto dto, Team team) {
        return Ban.create(dto.getChampionId(), dto.getPickTurn(), team);
    }
}
