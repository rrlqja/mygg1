package song.mygg1.domain.riot.dto.match;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import song.mygg1.domain.riot.entity.match.Matches;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchDto {
    private MetadataDto metadata;
    private InfoDto info;

    public Matches toEntity() {
        return Matches.create(metadata.toEntity(), info.toEntity());
    }

    public MatchDto(Matches matches) {
        this.metadata = new MetadataDto(matches.getMetadata());
        this.info = new InfoDto(matches.getInfo());
    }
}
