package song.mygg1.domain.riot.dto.champion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChampionRuneStatsResponse {
    private Integer championId;
    private Integer totalGames;
    private RuneStatDto primaryStyle;
    private RuneStatDto secondaryStyle;
    private List<RuneStatDto> primaryRunes = new ArrayList<>();
    private List<RuneStatDto> secondaryRunes = new ArrayList<>();
    private List<RuneStatDto> statPerks = new ArrayList<>();
}
