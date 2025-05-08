package song.mygg1.domain.riot.dto.champion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChampionDto {
    private Long key;
    private String id;
    private String name;
    private String title;
}
