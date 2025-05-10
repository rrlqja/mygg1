package song.mygg1.domain.riot.dto.summoner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import song.mygg1.domain.riot.entity.summoner.Summoner;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SummonerDto {
    private String id;
    private String accountId;
    private String puuid;
    private Integer profileIconId;
    private Long revisionDate;
    private Long summonerLevel;

    public Summoner toEntity() {
        return Summoner.create(id, accountId, puuid, profileIconId, revisionDate, summonerLevel);
    }
}
