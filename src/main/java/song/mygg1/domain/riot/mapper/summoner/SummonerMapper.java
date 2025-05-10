package song.mygg1.domain.riot.mapper.summoner;

import org.springframework.stereotype.Component;
import song.mygg1.domain.riot.dto.summoner.SummonerDto;
import song.mygg1.domain.riot.entity.summoner.Summoner;

@Component
public class SummonerMapper {
    public SummonerDto toDto(Summoner summoner) {
        SummonerDto dto = new SummonerDto();

        dto.setId(summoner.getId());
        dto.setAccountId(summoner.getAccountId());
        dto.setPuuid(summoner.getPuuid());
        dto.setProfileIconId(summoner.getProfileIconId());
        dto.setRevisionDate(summoner.getRevisionDate());
        dto.setSummonerLevel(summoner.getSummonerLevel());

        return dto;
    }

    public Summoner toEntity(SummonerDto dto) {
        if (dto == null) return null;
        return Summoner.create(
                dto.getId(),
                dto.getAccountId(),
                dto.getPuuid(),
                dto.getProfileIconId(),
                dto.getRevisionDate(),
                dto.getSummonerLevel()
        );
    }
}
