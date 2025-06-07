package song.mygg1.domain.riot.dto.match.participant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerkStyleDto {
    private String description;
    private List<PerkStyleSelectionDto> selections = new ArrayList<>();
    private Integer style;
}
