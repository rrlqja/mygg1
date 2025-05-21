package song.mygg1.domain.riot.dto.champion;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import song.mygg1.domain.riot.dto.match.info.ChampionUsageDto;
import song.mygg1.domain.riot.dto.match.participant.ChampionWinRatePerDateDto;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
public class ChampionInfoDto {
    private ChampionDto championDto;
    private List<ChampionWinRatePerDateDto> winRate = new ArrayList<>();
    private List<ChampionUsageDto> championUsageList = new ArrayList<>();

    public ChampionInfoDto(ChampionDto championDto, List<ChampionWinRatePerDateDto> winRate, List<ChampionUsageDto> championUsageList) {
        this.championDto = championDto;
        this.winRate = winRate;
        this.championUsageList = championUsageList;
    }
}
