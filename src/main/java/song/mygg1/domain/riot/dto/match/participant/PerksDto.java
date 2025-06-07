package song.mygg1.domain.riot.dto.match.participant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerksDto {
    private PerkStatsDto statPerks;
    private List<PerkStyleDto> styles;
}
