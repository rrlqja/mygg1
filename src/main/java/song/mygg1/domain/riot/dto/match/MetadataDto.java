package song.mygg1.domain.riot.dto.match;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import song.mygg1.domain.riot.entity.match.Metadata;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetadataDto {
    private String matchId;
    private String dataVersion;
    private List<String> participants = new ArrayList<>();

    public Metadata toEntity() {
        return Metadata.create(matchId, dataVersion, participants);
    }

    public MetadataDto(Metadata metadata) {
        this.matchId = metadata.getMatchId();
        this.dataVersion = metadata.getDataVersion();
        this.participants = metadata.getParticipants();
    }
}
