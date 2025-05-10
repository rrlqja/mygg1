package song.mygg1.domain.riot.mapper.match;

import org.springframework.stereotype.Component;
import song.mygg1.domain.riot.dto.match.MetadataDto;
import song.mygg1.domain.riot.entity.match.Metadata;

@Component
public class MetadataMapper {
    public MetadataDto toDto(Metadata metadata) {
        MetadataDto dto = new MetadataDto();

        dto.setMatchId(metadata.getMatchId());
        dto.setDataVersion(metadata.getDataVersion());
        dto.setParticipants(metadata.getParticipants());

        return dto;
    }

    public Metadata toEntity(MetadataDto dto) {
        return Metadata.create(dto.getMatchId(), dto.getDataVersion(), dto.getParticipants());
    }
}
