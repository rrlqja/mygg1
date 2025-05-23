package song.mygg1.domain.riot.dto.timeline.event;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import song.mygg1.domain.riot.dto.timeline.DamageInfoDto;
import song.mygg1.domain.riot.dto.timeline.EventsTimeLineDto;
import song.mygg1.domain.riot.dto.timeline.PositionDto;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("CHAMPION_KILL")
public class ChampionKillEventDto extends EventsTimeLineDto {
    private List<Integer> assistingParticipantIds = new ArrayList<>();
    private Integer bounty;
    private Integer killStreakLength;
    private Integer killerId;
    private PositionDto position;
    private Integer shutdownBounty;
    private List<DamageInfoDto> victimDamageDealt = new ArrayList<>();
    private List<DamageInfoDto> victimDamageReceived = new ArrayList<>();
    private Integer victimId;
}
