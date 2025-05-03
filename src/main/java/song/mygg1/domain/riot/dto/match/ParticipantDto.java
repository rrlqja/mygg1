package song.mygg1.domain.riot.dto.match;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import song.mygg1.domain.riot.entity.match.Info;
import song.mygg1.domain.riot.entity.match.Participant;

import java.util.Arrays;
import java.util.List;

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

    private Integer goldEarned;
    private Integer goldSpent;
    private Integer visionScore;
    private Integer visionWardsBoughtInGame;
    private Integer wardsKilled;
    private Integer wardsPlaced;

    private String summonerBaseUrl = "/image/champion/";
    private Integer summoner1Id;
    private Integer summoner2Id;

    public Participant toEntity(Info info) {
        return Participant.create(participantId, assists, deaths, kills, championName, summonerName, win, teamId,
                totalDamageDealt, totalDamageDealtToChampions, totalDamageTaken, totalHeal, totalHealsOnTeammates, totalMinionsKilled,
                item0, item1, item2, item3, item4, item5, item6, puuid, riotIdGameName, riotIdTagLine, summonerId,
                info, goldEarned, goldSpent, visionScore, visionWardsBoughtInGame, wardsKilled, wardsPlaced,
                summoner1Id, summoner2Id);
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
        this.goldEarned = participant.getGoldEarned();
        this.goldSpent = participant.getGoldSpent();
        this.visionScore = participant.getVisionScore();
        this.visionWardsBoughtInGame = participant.getVisionWardsBoughtInGame();
        this.wardsKilled = participant.getWardsKilled();
        this.wardsPlaced = participant.getWardsPlaced();
        this.summoner1Id = participant.getSummoner1Id();
        this.summoner2Id = participant.getSummoner2Id();
    }

    public List<Integer> getItemList() {
        return Arrays.asList(item0, item1, item2, item3, item4, item5);
    }

    public List<Integer> getSummonerIdList() {
        return Arrays.asList(summoner1Id, summoner2Id);
    }
}
