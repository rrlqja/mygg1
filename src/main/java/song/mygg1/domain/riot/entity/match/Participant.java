package song.mygg1.domain.riot.entity.match;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import song.mygg1.domain.riot.entity.match.participant.Perks;

@ToString(exclude = {"info"})
@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Participant {
    @EmbeddedId
    private ParticipantId id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "perks_id")
    private Perks perks;

    @JsonIgnore
    @MapsId("infoId")
    @JoinColumn(name = "info_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Info info;

    private Integer assists;
    private Integer deaths;
    private Integer kills;
    private Integer champLevel;
    private Integer championId;
    private String championName;
    private String summonerName;
    private Boolean win;
    private Integer teamId;
    private Integer totalDamageDealt;
    private Integer totalDamageDealtToChampions;
    private Integer totalDamageTaken;
    private Integer totalHeal;
    private Integer totalHealsOnTeammates;
    private Integer totalMinionsKilled;

    private Integer item0;
    private Integer item1;
    private Integer item2;
    private Integer item3;
    private Integer item4;
    private Integer item5;
    private Integer item6;

    private String puuid;
    private String riotIdGameName;
    private String riotIdTagline;

    private String summonerId;

    private Integer goldEarned;
    private Integer goldSpent;
    private Integer visionScore;
    private Integer visionWardsBoughtInGame;
    private Integer wardsKilled;
    private Integer wardsPlaced;

    private Integer summoner1Id;
    private Integer summoner2Id;

    public void setPerks(Perks perks) {
        this.perks = perks;
        if (perks != null) {
            perks.setParticipant(this);
        }
    }

    public static Participant create(Integer participantId, Integer assists, Integer deaths, Integer kills, Integer championLevel, Integer championId, String championName,
                                     String summonerName, Boolean win, Integer teamId, Integer totalDamageDealt, Integer totalDamageDealtToChampions,
                                     Integer totalDamageTaken, Integer totalHeal, Integer totalHealsOnTeammates, Integer totalMinionsKilled,
                                     Integer item0, Integer item1, Integer item2, Integer item3, Integer item4, Integer item5, Integer item6,
                                     String puuid, String riotIdGameName, String riotIdTagline, String summonerId,
                                     Info info, Integer goldEarned, Integer goldSpent,
                                     Integer visionScore, Integer visionWardsBoughtInGame, Integer wardsKilled, Integer wardsPlaced,
                                     Integer summoner1Id, Integer summoner2Id) {
        return new Participant(participantId, assists, deaths, kills, championLevel, championId, championName, summonerName, win, teamId, totalDamageDealt,
                totalDamageDealtToChampions, totalDamageTaken, totalHeal, totalHealsOnTeammates, totalMinionsKilled,
                item0, item1, item2, item3, item4, item5, item6, puuid, riotIdGameName, riotIdTagline, summonerId,
                info, goldEarned, goldSpent, visionScore, visionWardsBoughtInGame, wardsKilled, wardsPlaced,
                summoner1Id, summoner2Id);
    }

    private Participant(Integer participantId, Integer assists, Integer deaths, Integer kills, Integer champLevel, Integer championId, String championName,
                        String summonerName, Boolean win, Integer teamId, Integer totalDamageDealt, Integer totalDamageDealtToChampions,
                        Integer totalDamageTaken, Integer totalHeal, Integer totalHealsOnTeammates, Integer totalMinionsKilled,
                        Integer item0, Integer item1, Integer item2, Integer item3, Integer item4, Integer item5, Integer item6,
                        String puuid, String riotIdGameName, String riotIdTagline, String summonerId,
                        Info info, Integer goldEarned, Integer goldSpent,
                        Integer visionScore, Integer visionWardsBoughtInGame, Integer wardsKilled, Integer wardsPlaced,
                        Integer summoner1Id, Integer summoner2Id) {
        this.id = new ParticipantId(info.getGameId(), participantId);
        this.assists = assists;
        this.deaths = deaths;
        this.kills = kills;
        this.champLevel = champLevel;
        this.championId = championId;
        this.championName = championName;
        this.summonerName = summonerName;
        this.win = win;
        this.teamId = teamId;
        this.totalDamageDealt = totalDamageDealt;
        this.totalDamageDealtToChampions = totalDamageDealtToChampions;
        this.totalDamageTaken = totalDamageTaken;
        this.totalHeal = totalHeal;
        this.totalHealsOnTeammates = totalHealsOnTeammates;
        this.totalMinionsKilled = totalMinionsKilled;

        this.item0 = item0;
        this.item1 = item1;
        this.item2 = item2;
        this.item3 = item3;
        this.item4 = item4;
        this.item5 = item5;
        this.item6 = item6;

        this.puuid = puuid;
        this.riotIdGameName = riotIdGameName;
        this.riotIdTagline = riotIdTagline;
        this.summonerId = summonerId;

        this.info = info;

        this.goldEarned = goldEarned;
        this.goldSpent = goldSpent;
        this.visionScore = visionScore;
        this.visionWardsBoughtInGame = visionWardsBoughtInGame;
        this.wardsKilled = wardsKilled;
        this.wardsPlaced = wardsPlaced;

        this.summoner1Id = summoner1Id;
        this.summoner2Id = summoner2Id;
    }
}
