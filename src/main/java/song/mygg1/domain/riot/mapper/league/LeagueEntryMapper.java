package song.mygg1.domain.riot.mapper.league;

import org.springframework.stereotype.Component;
import song.mygg1.domain.riot.dto.league.LeagueEntryDto;
import song.mygg1.domain.riot.entity.league.LeagueEntry;

@Component
public class LeagueEntryMapper {
    public LeagueEntryDto toDto(LeagueEntry entity) {
        LeagueEntryDto dto = new LeagueEntryDto();

        dto.setLeagueId(entity.getLeagueId());
        dto.setSummonerId(entity.getSummonerId());
        dto.setPuuid(entity.getId().getPuuid());
        dto.setQueueType(entity.getId().getQueueType());
        dto.setTier(entity.getTier());
        dto.setRank(entity.getRank());
        dto.setLeaguePoints(entity.getLeaguePoints());
        dto.setWins(entity.getWins());
        dto.setLosses(entity.getLosses());
        dto.setHotStreak(entity.isHotStreak());
        dto.setVeteran(entity.isVeteran());
        dto.setFreshBlood(entity.isFreshBlood());
        dto.setInactive(entity.isInactive());

        return dto;
    }

    public LeagueEntry toEntity(LeagueEntryDto dto) {
        return LeagueEntry.create(
                dto.getQueueType(),
                dto.getPuuid(),
                dto.getLeagueId(),
                dto.getTier(),
                dto.getRank(),
                dto.getSummonerId(),
                dto.getLeaguePoints(),
                dto.getWins(),
                dto.getLosses(),
                dto.isHotStreak(),
                dto.isVeteran(),
                dto.isFreshBlood(),
                dto.isInactive()
        );
    }
}
