package song.mygg1.domain.riot.entity.summoner;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Summoner {
    @Id
    private String id;
    private String accountId;
    private String puuid;
    private Integer profileIconId;
    private Long revisionDate;
    private Long summonerLevel;

    public static Summoner create(String id, String accountId, String puuid, Integer profileIconId, Long revisionDate, Long summonerLevel) {
        return new Summoner(id, accountId, puuid, profileIconId, revisionDate, summonerLevel);
    }

    private Summoner(String id, String accountId, String puuid, Integer profileIconId, Long revisionDate, Long summonerLevel) {
        this.id = id;
        this.accountId = accountId;
        this.puuid = puuid;
        this.profileIconId = profileIconId;
        this.revisionDate = revisionDate;
        this.summonerLevel = summonerLevel;
    }
}
