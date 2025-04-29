package song.mygg1.domain.riot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import song.mygg1.domain.riot.entity.Info;
import song.mygg1.domain.riot.entity.Participant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantDto {
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

    public Participant toEntity(Info info) {
        return Participant.create(participantId, assists, deaths, kills, championName, summonerName, win, teamId,
                totalDamageDealt, totalDamageDealtToChampions, totalDamageTaken, totalHeal, totalHealsOnTeammates, totalMinionsKilled,
                item0, item1, item2, item3, item4, item5, item6, puuid, riotIdGameName, riotIdTagLine, summonerId,
                info);
    }

    public ParticipantDto(Participant participant) {
        this.participantId = participant.getParticipantId();
        this.assists = participant.getAssists();
        this.deaths = participant.getDeaths();
        this.kills = participant.getKills();
        this.championName = participant.getChampionName();
        this.summonerName = participant.getSummonerName();
        this.win = participant.getWin();
        this.teamId = participant.getTeamId();
        this.totalDamageDealt = participant.getTotalDamageDealt();
        this.totalDamageTaken = participant.getTotalDamageTaken();
        this.totalHeal = participant.getTotalHeal();
        this.totalHealsOnTeammates = participant.getTotalHealsOnTeammates();
        this.totalMinionsKilled = participant.getTotalMinionsKilled();
        this.item0 = participant.getItem0();
        this.item1 = participant.getItem1();
        this.item2 = participant.getItem2();
        this.item3 = participant.getItem3();
        this.item4 = participant.getItem4();
        this.item5 = participant.getItem5();
        this.item6 = participant.getItem6();
        this.puuid = participant.getPuuid();
        this.riotIdGameName = participant.getRiotIdGameName();
        this.riotIdTagLine = participant.getRiotIdTagLine();
        this.summonerId = participant.getSummonerId();
    }
}
