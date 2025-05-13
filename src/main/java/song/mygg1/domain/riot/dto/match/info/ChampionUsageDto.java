package song.mygg1.domain.riot.dto.match.info;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@NoArgsConstructor
public class ChampionUsageDto {
    private Object date;
    private Object totalMatches;
    private Object pickCount;
    private Object pickRate;

    public ChampionUsageDto(Object date, Object totalMatches, Object pickCount, Object pickRate) {
        this.date = date;
        this.totalMatches = totalMatches;
        this.pickCount = pickCount;
        this.pickRate = pickRate;
    }
}
