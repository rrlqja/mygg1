package song.mygg1.domain.riot.dto.timeline;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParticipantFramesDto {
    private final Map<String, ParticipantFrameDto> frames = new HashMap<>();

    @JsonAnySetter
    public void setFrame(String participantId, ParticipantFrameDto frame) {
        frames.put(participantId, frame);
    }

    @JsonAnyGetter
    public Map<String, ParticipantFrameDto> getFrames() {
        return frames;
    }
}
