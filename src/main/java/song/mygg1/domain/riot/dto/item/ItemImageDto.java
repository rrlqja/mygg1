package song.mygg1.domain.riot.dto.item;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemImageDto {
    private String full;
    private String sprite;
    private String group;
    private Integer x;
    private Integer y;
    private Integer w;
    private Integer h;
}
