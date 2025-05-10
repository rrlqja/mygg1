package song.mygg1.domain.riot.dto.match;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchDto {
    private String matchId;
    private MetadataDto metadata;
    private InfoDto info;
    private boolean isWin;
    private ParticipantDto player;
    private String championImgUrl;
    private String kda;
    private String kdaAvg;
    private List<ChampionImgInfoDto> participantChampionImgList;
}
