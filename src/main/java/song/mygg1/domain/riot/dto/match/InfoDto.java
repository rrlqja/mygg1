package song.mygg1.domain.riot.dto.match;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import song.mygg1.domain.riot.dto.match.team.TeamDto;
import song.mygg1.domain.riot.entity.QueueType;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InfoDto {
    private Long gameId;
    private String endOfGameResult;
    private String gameMode;
    private String gameName;
    private Long gameStartTimestamp;
    private String gameType;
    private String gameVersion;
    private int queueId;
    private Long gameCreation;
    private Long gameDuration;
    private Long gameEndTimestamp;

    private List<ParticipantDto> participants = new ArrayList<>();
    private List<TeamDto> teams = new ArrayList<>();

    private Integer maxDealt;
    private Integer maxTaken;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public String getQueueType() {
        return QueueType.fromId(queueId).getDisplayName();
    }
}
