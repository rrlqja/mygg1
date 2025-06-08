package song.mygg1.domain.riot.dto.rune;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RuneDto {
    private Integer id;
    private String key;
    private String name;
    private String icon;
    private String shortDesc;
    private String longDesc;
}
