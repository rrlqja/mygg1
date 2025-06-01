package song.mygg1.domain.riot.dto.champion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChampionBanPickDto {
    private Long championId;
    private String championName;
    private Long totalGames;
    private Long totalBanned;
    private Long totalPicked;
    private Long totalWin;
    private Double banRate;
    private Double pickRate;
    private Double winRate;
    private String tier;

    public ChampionBanPickDto(Long championId, String championName, Long totalGames, Long totalBanned, Long totalPicked, Long totalWin, Double banRate, Double pickRate, Double winRate) {
        this.championId = championId;
        this.championName = championName;
        this.totalGames = totalGames;
        this.totalBanned = totalBanned;
        this.totalPicked = totalPicked;
        this.totalWin = totalWin;
        this.banRate = banRate;
        this.pickRate = pickRate;
        this.winRate = winRate;
        this.tier = "S";
    }
}
