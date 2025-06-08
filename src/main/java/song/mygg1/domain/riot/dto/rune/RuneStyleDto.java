package song.mygg1.domain.riot.dto.rune;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RuneStyleDto {
    private Integer id;
    private String key;
    private String name;
    private String icon;
    private List<RuneSlotDto> slots = new ArrayList<>();
}
