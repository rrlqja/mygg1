package song.mygg1.domain.riot.dto.timeline;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventsTimeLineDto {
    private Long timestamp;
    private Long realTimestamp;
    private String type;
}
