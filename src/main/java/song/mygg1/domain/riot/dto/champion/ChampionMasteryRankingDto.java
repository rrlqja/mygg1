package song.mygg1.domain.riot.dto.champion;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import song.mygg1.domain.riot.dto.account.AccountDto;
import song.mygg1.domain.riot.dto.summoner.SummonerDto;

@ToString
@Data
@NoArgsConstructor
public class ChampionMasteryRankingDto {
    private Object championId;
    private Object championLevel;
    private Object championPoints;
    private String puuid;
    private Long totalCount;
    private AccountDto account;
    private SummonerDto summoner;

    public ChampionMasteryRankingDto(Object championId, Object championLevel, Object championPoints, String puuid, Long totalCount) {
        this.championId = championId;
        this.championLevel = championLevel;
        this.championPoints = championPoints;
        this.puuid = puuid;
        this.totalCount = totalCount;
    }
}
