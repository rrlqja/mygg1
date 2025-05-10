package song.mygg1.domain.riot.dto.league;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeagueItemDto {
    private String summonerId;
    private String puuid;
    private Integer leaguePoints;
    private String rank;
    private Integer wins;
    private Integer losses;
    private boolean veteran;
    private boolean inactive;
    private boolean freshBlood;
    private boolean hotStreak;
}
