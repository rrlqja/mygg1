package song.mygg1.domain.riot.entity.league;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString(exclude = {"leagueList"})
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LeagueItem {
    @Id
    private String summonerId;

    private String puuid;
    private Integer leaguePoints;
    @Column(name = "league_rank")
    private String rank;
    private Integer wins;
    private Integer losses;
    private boolean veteran;
    private boolean inactive;
    private boolean freshBlood;
    private boolean hotStreak;

    @JoinColumn(name = "league_list_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private LeagueList leagueList;

    public static LeagueItem create(LeagueList leagueList, String summonerId, String puuid, Integer leaguePoints, String rank, Integer wins, Integer losses,
                                    boolean veteran, boolean inactive, boolean freshBlood, boolean hotStreak) {
        return new LeagueItem(leagueList, summonerId, puuid, leaguePoints, rank, wins, losses, veteran, inactive, freshBlood, hotStreak);
    }

    private LeagueItem(LeagueList leagueList, String summonerId, String puuid, Integer leaguePoints, String rank, Integer wins, Integer losses,
                       boolean veteran, boolean inactive, boolean freshBlood, boolean hotStreak) {
        setLeagueList(leagueList);
        this.summonerId = summonerId;
        this.puuid = puuid;
        this.leaguePoints = leaguePoints;
        this.rank = rank;
        this.wins = wins;
        this.losses = losses;
        this.veteran = veteran;
        this.inactive = inactive;
        this.freshBlood = freshBlood;
        this.hotStreak = hotStreak;
    }

    private void setLeagueList(LeagueList leagueList) {
        this.leagueList = leagueList;
        leagueList.getEntries().add(this);
    }
}
