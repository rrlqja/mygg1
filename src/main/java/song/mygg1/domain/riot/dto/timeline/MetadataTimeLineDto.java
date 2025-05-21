package song.mygg1.domain.riot.dto.timeline;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetadataTimeLineDto {
    private String dataVersion;
    private String matchId;
    private List<String> participants = new ArrayList<>();
}
