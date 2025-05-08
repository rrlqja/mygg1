package song.mygg1.domain.riot.dto.league;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import song.mygg1.domain.riot.entity.league.LeagueItem;
import song.mygg1.domain.riot.entity.league.LeagueList;

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

    public LeagueItem toEntity(LeagueList leagueList) {
        return LeagueItem.create(leagueList, summonerId, puuid, leaguePoints, rank, wins, losses, veteran, inactive, freshBlood, hotStreak);
    }

    public LeagueItemDto(LeagueItem leagueItem) {
        this.summonerId = leagueItem.getSummonerId();
        this.puuid = leagueItem.getPuuid();
        this.leaguePoints = leagueItem.getLeaguePoints();
        this.rank = leagueItem.getRank();
        this.wins = leagueItem.getWins();
        this.losses = leagueItem.getLosses();
        this.veteran = leagueItem.isVeteran();
        this.inactive = leagueItem.isInactive();
        this.freshBlood = leagueItem.isFreshBlood();
        this.hotStreak = leagueItem.isHotStreak();
    }
}
