package song.mygg1.domain.riot.dto.timeline;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InfoTimeLineDto {
    private Long gameId;
    private String endOfGameResult;
    private Long frameInterval;
    private List<ParticipantTimeLineDto> participants = new ArrayList<>();
    private List<FramesTimeLineDto> frames = new ArrayList<>();
}
