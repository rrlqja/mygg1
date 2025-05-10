package song.mygg1.domain.riot.mapper.league;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import song.mygg1.domain.riot.dto.league.LeagueListDto;
import song.mygg1.domain.riot.entity.league.LeagueList;

@Component
@RequiredArgsConstructor
public class LeagueListMapper {
    private final LeagueItemMapper itemMapper;

    public LeagueListDto toDto(LeagueList entity) {
        LeagueListDto dto = new LeagueListDto();

        dto.setLeagueId(entity.getLeagueId());
        dto.setTier(entity.getTier());
        dto.setQueue(entity.getQueue());
        dto.setName(entity.getName());
        dto.setEntries(entity.getEntries().stream()
                .map(itemMapper::toDto)
                .toList());

        return dto;
    }

    public LeagueList toEntity(LeagueListDto dto) {
        LeagueList entity = LeagueList.create(
                dto.getLeagueId(),
                dto.getTier(),
                dto.getQueue(),
                dto.getName());

        dto.getEntries().forEach(itemDto ->
                itemMapper.toEntity(itemDto, entity)
        );

        return entity;
    }
}
