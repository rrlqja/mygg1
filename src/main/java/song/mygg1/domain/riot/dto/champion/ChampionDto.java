package song.mygg1.domain.riot.dto.champion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import song.mygg1.domain.riot.entity.champion.Champion;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChampionDto {
    private Long key;
    private String id;
    private String name;
    private String title;

    public ChampionDto(Champion champion) {
        this.key = champion.getKey();
        this.id = champion.getId();
        this.name = champion.getName();
        this.title = champion.getTitle();
    }
}
