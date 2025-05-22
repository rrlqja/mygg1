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
public class FramesTimeLineDto {
    private Long timestamp;
    private List<EventsTimeLineDto> events = new ArrayList<>();
    private ParticipantFramesDto participantFrames;
}
