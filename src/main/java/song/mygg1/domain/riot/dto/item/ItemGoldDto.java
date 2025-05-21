package song.mygg1.domain.riot.dto.item;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemGoldDto {
    private Integer base;
    private Boolean purchasable;
    private Integer total;
    private Integer sell;
}
