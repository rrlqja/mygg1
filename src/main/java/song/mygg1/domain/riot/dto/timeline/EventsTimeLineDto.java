package song.mygg1.domain.riot.dto.timeline;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventsTimeLineDto {
    private Long timestamp;
    private Long realTimestamp;
    private String type;
}
