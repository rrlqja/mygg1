package song.mygg1.domain.riot.dto.match.participant;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;
import java.time.LocalDate;

@ToString
@Data
@NoArgsConstructor
public class ChampionWinRatePerDateDto {
    private LocalDate date;
    private String championName;
    private Long totalGames;
    private Long totalWins;
    private Long totalLosses;
    private Double winRate;

    public ChampionWinRatePerDateDto(Date date, String championName, Long totalGames, Long totalWins, Long totalLosses, Double winRate) {
        this.date = date.toLocalDate();
        this.championName = championName;
        this.totalGames = totalGames;
        this.totalWins = totalWins;
        this.totalLosses = totalLosses;
        this.winRate = winRate;
    }
}
