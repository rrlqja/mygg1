package song.mygg1.domain.riot.dto.match.team;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BanDto {
    private Integer championId;
    private Integer pickTurn;
}
