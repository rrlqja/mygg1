package song.mygg1.domain.riot.dto.match;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetadataDto {
    private String matchId;
    private String dataVersion;
    private List<String> participants = new ArrayList<>();
}
