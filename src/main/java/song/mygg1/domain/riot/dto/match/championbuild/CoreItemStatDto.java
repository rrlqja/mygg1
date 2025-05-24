package song.mygg1.domain.riot.dto.match.championbuild;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoreItemStatDto {
    private Integer itemId;
    private String itemName;
    private Double purchasePercentage;
    private Double winRate;
    private Integer purchaseCount;
}
