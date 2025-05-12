package song.mygg1.domain.riot.dto.match.participant;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;

@ToString
@Data
@NoArgsConstructor
public class WinRateDto {
    private String championName;
    private Integer championId;
    private Long totalGames;
    private Long wins;
    private Double winRate;
    private Long totalGamesPrev;
    private Long winsPrev;
    private Double winRatePrev;
    private Double winRateDiff;

    public WinRateDto(String championName, Integer championId, Long totalGames, Long wins, Double winRate, Long totalGamesPrev, Long winsPrev, Double winRatePrev) {
        this.championName = championName;
        this.championId = championId;
        this.totalGames = totalGames;
        this.wins = wins;
        this.winRate = winRate;
        this.totalGamesPrev = totalGamesPrev;
        this.winsPrev = winsPrev;
        this.winRatePrev = winRatePrev;
        this.winRateDiff = BigDecimal
                .valueOf(winRate)
                .subtract(BigDecimal.valueOf(winRatePrev))
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
