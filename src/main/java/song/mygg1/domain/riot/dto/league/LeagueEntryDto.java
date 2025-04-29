package song.mygg1.domain.riot.dto.league;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import song.mygg1.domain.riot.entity.league.LeagueEntry;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeagueEntryDto {
    private String leagueId;
    private String summonerId;
    private String puuid;
    private String queueType;
    private String tier;
    private String rank;
    private int leaguePoints;
    private int wins;
    private int losses;
    private boolean hotStreak;
    private boolean veteran;
    private boolean freshBlood;
    private boolean inactive;

    public LeagueEntry toEntity() {
        return LeagueEntry.create(queueType, puuid, leagueId, tier, rank, summonerId, leaguePoints, wins, losses, hotStreak, veteran, freshBlood, inactive);
    }

    public LeagueEntryDto(LeagueEntry leagueEntry) {
        this.leagueId = leagueEntry.getLeagueId();
        this.summonerId = leagueEntry.getSummonerId();
        this.puuid = leagueEntry.getId().getPuuid();
        this.queueType = leagueEntry.getId().getQueueType();
        this.tier = leagueEntry.getTier();
        this.rank = leagueEntry.getRank();
        this.leaguePoints = leagueEntry.getLeaguePoints();
        this.wins = leagueEntry.getWins();
        this.losses = leagueEntry.getLosses();
        this.hotStreak = leagueEntry.isHotStreak();
        this.veteran = leagueEntry.isVeteran();
        this.freshBlood = leagueEntry.isFreshBlood();
        this.inactive = leagueEntry.isInactive();
    }
}
