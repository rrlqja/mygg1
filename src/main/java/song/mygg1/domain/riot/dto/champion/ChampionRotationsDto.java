package song.mygg1.domain.riot.dto.champion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChampionRotationsDto {
    private List<Integer> freeChampionIds = new ArrayList<>();
    private List<Integer> freeChampionIdsForNewPlayers = new ArrayList<>();
}
