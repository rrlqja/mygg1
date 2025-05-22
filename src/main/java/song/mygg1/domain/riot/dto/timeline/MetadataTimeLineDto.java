package song.mygg1.domain.riot.dto.timeline;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MetadataTimeLineDto {
    private String dataVersion;
    private String matchId;
    private List<String> participants = new ArrayList<>();
}
