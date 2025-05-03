package song.mygg1.domain.riot.dto.match;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import song.mygg1.domain.riot.entity.QueueType;
import song.mygg1.domain.riot.entity.match.Info;
import song.mygg1.domain.riot.entity.match.Participant;
import song.mygg1.domain.riot.entity.match.Team;

import java.util.ArrayList;
import java.util.List;

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

    public Info toEntity() {
        Info info = Info.create(gameId, endOfGameResult, gameMode, gameName, gameStartTimestamp, gameType, gameVersion, queueId,
                gameCreation, gameDuration, gameEndTimestamp);

        List<Participant> participantList = participants.stream()
                .map(participantDto -> participantDto.toEntity(info))
                .toList();

        List<Team> teamList = teams.stream()
                .map(teamDto -> teamDto.toEntity(info))
                .toList();

        info.setParticipants(participantList);
        info.setTeams(teamList);

        return info;
    }

    public InfoDto(Info info) {
        this.gameId = info.getGameId();
        this.endOfGameResult = info.getEndOfGameResult();
        this.gameMode = info.getGameMode();
        this.gameName = info.getGameName();
        this.gameStartTimestamp = info.getGameStartTimestamp();
        this.gameType = info.getGameType();
        this.gameVersion = info.getGameVersion();
        this.queueId = info.getQueueId();

        this.participants = info.getParticipants().stream()
                .map(ParticipantDto::new)
                .toList();

        this.teams = info.getTeams().stream()
                .map(TeamDto::new)
                .toList();

        this.gameCreation = info.getGameCreation();
        this.gameDuration = info.getGameDuration();
        this.gameEndTimestamp = info.getGameEndTimestamp();
    }

    public String getGameDuration() {
        long minutes = gameDuration / 60;
        long seconds = gameDuration % 60;
        return String.format("%d분 %d초", minutes, seconds);
    }

    public String getQueueType() {
        return QueueType.fromId(queueId).getDisplayName();
    }
}
