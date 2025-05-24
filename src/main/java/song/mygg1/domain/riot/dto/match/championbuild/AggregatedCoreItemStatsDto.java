package song.mygg1.domain.riot.dto.match.championbuild;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AggregatedCoreItemStatsDto {
    private List<CoreItemStatDto> firstCoreItemStats = new ArrayList<>();
    private List<CoreItemStatDto> secondCoreItemStats = new ArrayList<>();
    private List<CoreItemStatDto> thirdCoreItemStats = new ArrayList<>();
}
