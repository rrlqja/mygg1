package song.mygg1.domain.riot.mapper.match;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import song.mygg1.domain.riot.dto.match.InfoDto;
import song.mygg1.domain.riot.dto.match.ParticipantDto;
import song.mygg1.domain.riot.dto.match.team.TeamDto;
import song.mygg1.domain.riot.entity.match.Info;
import song.mygg1.domain.riot.entity.match.Participant;
import song.mygg1.domain.riot.entity.match.team.Team;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InfoMapper {
    private final ParticipantMapper participantMapper;
    private final TeamMapper teamMapper;

    public InfoDto toDto(Info info) {
        InfoDto dto = new InfoDto();

        dto.setGameId(info.getGameId());
        dto.setEndOfGameResult(info.getEndOfGameResult());
        dto.setGameMode(info.getGameMode());
        dto.setGameName(info.getGameName());
        dto.setGameStartTimestamp(info.getGameStartTimestamp());
        dto.setGameType(info.getGameType());
        dto.setGameVersion(info.getGameVersion());
        dto.setQueueId(info.getQueueId());
        dto.setGameCreation(info.getGameCreation());
        dto.setGameDuration(info.getGameDuration());
        dto.setGameEndTimestamp(info.getGameEndTimestamp());

        List<ParticipantDto> parts = info.getParticipants().stream()
                .map(participantMapper::toDto)
                .toList();
        int maxDealt = parts.stream().mapToInt(ParticipantDto::getTotalDamageDealtToChampions).max().orElse(1);
        int maxTaken = parts.stream().mapToInt(ParticipantDto::getTotalDamageTaken).max().orElse(1);
        parts.forEach(p -> {
            p.setDealtPercent(p.getTotalDamageDealtToChampions() * 100.0 / maxDealt);
            p.setTakenPercent(p.getTotalDamageTaken() * 100.0 / maxTaken);
        });
        dto.setParticipants(parts);

        List<TeamDto> teams = info.getTeams().stream()
                .map(teamMapper::toDto)
                .collect(Collectors.toList());
        dto.setTeams(teams);

        return dto;
    }

    public Info toEntity(InfoDto dto) {
        Info info = Info.create(
                dto.getGameId(), dto.getEndOfGameResult(), dto.getGameMode(), dto.getGameName(),
                dto.getGameStartTimestamp(), dto.getGameType(), dto.getGameVersion(), dto.getQueueId(),
                dto.getGameCreation(), dto.getGameDuration(), dto.getGameEndTimestamp()
        );

        List<Participant> participantList = dto.getParticipants().stream()
                .map(p -> participantMapper.toEntity(p, info))
                .collect(Collectors.toList());
        info.setParticipants(participantList);

        List<Team> teamList = dto.getTeams().stream()
                .map(t -> teamMapper.toEntity(t, info))
                .collect(Collectors.toList());
        info.setTeams(teamList);

        return	info;
    }
}
