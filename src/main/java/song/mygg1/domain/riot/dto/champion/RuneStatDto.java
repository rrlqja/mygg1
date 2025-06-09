package song.mygg1.domain.riot.dto.champion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RuneStatDto {
    private Integer runeId;
    private Double pickRate;
    private Integer pickCount;
}
