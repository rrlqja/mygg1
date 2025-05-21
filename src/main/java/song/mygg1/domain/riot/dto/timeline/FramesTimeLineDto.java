package song.mygg1.domain.riot.dto.timeline;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FramesTimeLineDto {
    private Integer timestamp;
    private List<EventsTimeLineDto> events = new ArrayList<>();
    private ParticipantFramesDto participantFrames;
}
