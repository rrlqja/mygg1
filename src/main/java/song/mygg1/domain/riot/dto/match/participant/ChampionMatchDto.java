package song.mygg1.domain.riot.dto.match.participant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import song.mygg1.domain.riot.dto.match.ChampionImgInfoDto;
import song.mygg1.domain.riot.dto.match.InfoDto;
import song.mygg1.domain.riot.dto.match.MetadataDto;
import song.mygg1.domain.riot.dto.match.ParticipantDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChampionMatchDto {
    private String matchId;
    private MetadataDto metadata;
    private InfoDto info;
    private Integer championId;
    private String championName;
    private boolean isWin;
    private String championImgUrl;
    private ParticipantDto player;
    private String kda;
    private String kdaAvg;
    private List<ChampionImgInfoDto> participantChampionImgList;
}
