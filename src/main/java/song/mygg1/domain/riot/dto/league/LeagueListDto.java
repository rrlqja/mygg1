package song.mygg1.domain.riot.dto.league;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import song.mygg1.domain.riot.entity.league.LeagueItem;
import song.mygg1.domain.riot.entity.league.LeagueList;

import java.util.ArrayList;
import java.util.List;

@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeagueListDto {
    private String leagueId;
    private String tier;
    private String queue;
    private String name;
    private List<LeagueItemDto> entries = new ArrayList<>();

    public LeagueList toEntity() {
        LeagueList leagueList = LeagueList.create(leagueId, tier, queue, name);

        List<LeagueItem> leagueItemList = entries.stream()
                .map(lid -> lid.toEntity(leagueList))
                .toList();

        return leagueList;
    }

    public LeagueListDto(LeagueList leagueList) {
        this.leagueId = leagueList.getLeagueId();
        this.tier = leagueList.getTier();
        this.queue = leagueList.getQueue();
        this.name = leagueList.getName();
        this.entries = leagueList.getEntries().stream()
                .map(LeagueItemDto::new)
                .toList();
    }
}
