package song.mygg1.domain.riot.dto.match.championbuild;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MatchPlayerInfo {
    private String matchId;
    private Integer participantId;
    private Boolean win;
}
