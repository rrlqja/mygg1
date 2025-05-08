package song.mygg1.domain.riot.dto.league;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import song.mygg1.domain.riot.dto.champion.ChampionMasteryDto;
import song.mygg1.domain.riot.entity.account.Account;
import song.mygg1.domain.riot.entity.league.LeagueItem;
import song.mygg1.domain.riot.entity.champion.ChampionMastery;
import song.mygg1.domain.riot.entity.summoner.Summoner;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeagueItemSummonerDto {
    private String summonerId;
    private String puuid;
    private Integer leaguePoints;
    private String rank;
    private Integer wins;
    private Integer losses;

    private String accountId;
    private Integer profileIconId;
    private Long summonerLevel;

    private String gameName;
    private String tagLine;

    private List<ChampionMasteryDto> championMasteryList = new ArrayList<>();

    public LeagueItemSummonerDto(LeagueItem leagueItem, Summoner summoner, Account account, List<ChampionMastery> championMasteryList) {
        this.summonerId = leagueItem.getSummonerId();
        this.puuid = leagueItem.getPuuid();
        this.leaguePoints = leagueItem.getLeaguePoints();
        this.rank = leagueItem.getRank();
        this.wins = leagueItem.getWins();
        this.losses = leagueItem.getLosses();

        this.accountId = summoner.getAccountId();
        this.profileIconId = summoner.getProfileIconId();
        this.summonerLevel = summoner.getSummonerLevel();

        this.gameName = account.getGameName();
        this.tagLine = account.getTagLine();

        this.championMasteryList = championMasteryList.stream().map(ChampionMasteryDto::new).toList();
    }
}
