package song.mygg1.domain.riot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import song.mygg1.domain.riot.entity.Info;
import song.mygg1.domain.riot.entity.Participant;
import song.mygg1.domain.riot.entity.Team;

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

    private List<ParticipantDto> participants = new ArrayList<>();
    private List<TeamDto> teams = new ArrayList<>();

    public Info toEntity() {
        Info info = Info.create(gameId, endOfGameResult, gameMode, gameName, gameStartTimestamp, gameType, gameVersion);

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

        this.participants = info.getParticipants().stream()
                .map(ParticipantDto::new)
                .toList();

        this.teams = info.getTeams().stream()
                .map(TeamDto::new)
                .toList();
    }
}
