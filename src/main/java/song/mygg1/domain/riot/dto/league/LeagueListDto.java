package song.mygg1.domain.riot.dto.league;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeagueListDto {
    private String leagueId;
    private String tier;
    private String queue;
    private String name;
    private List<LeagueItemDto> entries = new ArrayList<>();
}
