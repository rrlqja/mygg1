package song.mygg1.domain.riot.dto.match.participant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerkStatsDto {
    private Integer defense;
    private Integer flex;
    private Integer offense;
}
