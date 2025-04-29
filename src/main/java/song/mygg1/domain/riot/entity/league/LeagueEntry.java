package song.mygg1.domain.riot.entity.league;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LeagueEntry {
    @EmbeddedId
    private LeagueEntryId id;

    private String leagueId;
    private String tier;
    @Column(name = "league_rank")
    private String rank;
    private String summonerId;
    private int leaguePoints;
    private int wins;
    private int losses;
    private boolean hotStreak;
    private boolean veteran;
    private boolean freshBlood;
    private boolean inactive;

    public static LeagueEntry create(String queueType, String puuid, String leagueId, String tier, String rank, String summonerId,
                                     int leaguePoints, int wins, int losses, boolean hotStreak, boolean veteran, boolean freshBlood, boolean inactive) {
        LeagueEntryId id = new LeagueEntryId(queueType, puuid);
        return new LeagueEntry(id, leagueId, tier, rank, summonerId, leaguePoints, wins, losses, hotStreak, veteran, freshBlood, inactive);
    }

    private LeagueEntry(LeagueEntryId id, String leagueId, String tier, String rank, String summonerId,
                        int leaguePoints, int wins, int losses, boolean hotStreak, boolean veteran, boolean freshBlood, boolean inactive) {
        this.id = id;
        this.leagueId = leagueId;
        this.tier = tier;
        this.rank = rank;
        this.summonerId = summonerId;
        this.leaguePoints = leaguePoints;
        this.wins = wins;
        this.losses = losses;
        this.hotStreak = hotStreak;
        this.veteran = veteran;
        this.freshBlood = freshBlood;
        this.inactive = inactive;
    }
}
