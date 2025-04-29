package song.mygg1.domain.riot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Participant {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "info_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Info info;

    private Integer participantId;
    private Integer assists;
    private Integer deaths;
    private Integer kills;
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
    private String riotIdTagLine;

    private String summonerId;

    public static Participant create(Integer participantId, Integer assists, Integer deaths, Integer kills, String championName,
                                     String summonerName, Boolean win, Integer teamId, Integer totalDamageDealt, Integer totalDamageDealtToChampions,
                                     Integer totalDamageTaken, Integer totalHeal, Integer totalHealsOnTeammates, Integer totalMinionsKilled,
                                     Integer item0, Integer item1, Integer item2, Integer item3, Integer item4, Integer item5, Integer item6,
                                     String puuid, String riotIdGameName, String riotIdTagLine, String summonerId,
                                     Info info) {
        return new Participant(participantId, assists, deaths, kills, championName, summonerName, win, teamId, totalDamageDealt,
                totalDamageDealtToChampions, totalDamageTaken, totalHeal, totalHealsOnTeammates, totalMinionsKilled,
                item0, item1, item2, item3, item4, item5, item6, puuid, riotIdGameName, riotIdTagLine, summonerId,
                info);
    }

    private Participant(Integer participantId, Integer assists, Integer deaths, Integer kills, String championName,
                        String summonerName, Boolean win, Integer teamId, Integer totalDamageDealt, Integer totalDamageDealtToChampions,
                        Integer totalDamageTaken, Integer totalHeal, Integer totalHealsOnTeammates, Integer totalMinionsKilled,
                        Integer item0, Integer item1, Integer item2, Integer item3, Integer item4, Integer item5, Integer item6,
                        String puuid, String riotIdGameName, String riotIdTagLine, String summonerId,
                        Info info) {
        this.participantId = participantId;
        this.assists = assists;
        this.deaths = deaths;
        this.kills = kills;
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
        this.riotIdTagLine = riotIdTagLine;
        this.summonerId = summonerId;

        this.info = info;
    }
}
