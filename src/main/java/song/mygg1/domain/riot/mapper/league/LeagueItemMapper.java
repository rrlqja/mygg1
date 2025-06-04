package song.mygg1.domain.riot.mapper.league;

import org.springframework.stereotype.Component;
import song.mygg1.domain.riot.dto.league.LeagueItemDto;
import song.mygg1.domain.riot.entity.league.LeagueItem;
import song.mygg1.domain.riot.entity.league.LeagueList;

@Component
public class LeagueItemMapper {
    public LeagueItemDto toDto(LeagueItem item) {
        LeagueItemDto dto = new LeagueItemDto();

        dto.setSummonerId(item.getSummonerId());
        dto.setPuuid(item.getPuuid());
        dto.setLeaguePoints(item.getLeaguePoints());
        dto.setRank(item.getRank());
        dto.setWins(item.getWins());
        dto.setLosses(item.getLosses());
        dto.setVeteran(item.isVeteran());
        dto.setInactive(item.isInactive());
        dto.setFreshBlood(item.isFreshBlood());
        dto.setHotStreak(item.isHotStreak());

        dto.setTier(item.getLeagueList().getTier());

        return dto;
    }

    public LeagueItem toEntity(LeagueItemDto dto, LeagueList parent) {
        return LeagueItem.create(
                parent,
                dto.getSummonerId(),
                dto.getPuuid(),
                dto.getLeaguePoints(),
                dto.getRank(),
                dto.getWins(),
                dto.getLosses(),
                dto.isVeteran(),
                dto.isInactive(),
                dto.isFreshBlood(),
                dto.isHotStreak()
        );
    }
}
