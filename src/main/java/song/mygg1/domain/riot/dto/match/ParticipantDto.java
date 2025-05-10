package song.mygg1.domain.riot.dto.match;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantDto {
    private Long infoId;
    private Integer participantId;
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

    private String summonerBaseUrl = "/image/champion/";
    private Integer summoner1Id;
    private Integer summoner2Id;

    private String kda;
    private String kdaAvg;

    private double dealtPercent;
    private double takenPercent;

    public List<Integer> getItemList() {
        return Arrays.asList(item0, item1, item2, item3, item4, item5);
    }

    public void setItemList(List<Integer> itemList) {
        this.item0 = (itemList != null && itemList.size() > 0) ? itemList.get(0) : null;
        this.item1 = (itemList != null && itemList.size() > 1) ? itemList.get(1) : null;
        this.item2 = (itemList != null && itemList.size() > 2) ? itemList.get(2) : null;
        this.item3 = (itemList != null && itemList.size() > 3) ? itemList.get(3) : null;
        this.item4 = (itemList != null && itemList.size() > 4) ? itemList.get(4) : null;
        this.item5 = (itemList != null && itemList.size() > 5) ? itemList.get(5) : null;
    }

    public List<Integer> getSummonerIdList() {
        return Arrays.asList(summoner1Id, summoner2Id);
    }

    public void setSummonerIdList(List<Integer> summonerIdList) {
        this.summoner1Id = (summoner2Id != null && summonerIdList.size() > 0) ? summonerIdList.get(0) : null;
        this.summoner1Id = (summoner2Id != null && summonerIdList.size() > 1) ? summonerIdList.get(1) : null;
    }

    public void setDealtPercent(double dealtPercent) {
        this.dealtPercent = dealtPercent;
    }
    public void setTakenPercent(double takenPercent) {
        this.takenPercent = takenPercent;
    }

}
