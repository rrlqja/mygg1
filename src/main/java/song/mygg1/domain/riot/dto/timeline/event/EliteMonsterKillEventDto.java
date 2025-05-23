package song.mygg1.domain.riot.dto.timeline.event;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import song.mygg1.domain.riot.dto.timeline.EventsTimeLineDto;
import song.mygg1.domain.riot.dto.timeline.PositionDto;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("ELITE_MONSTER_KILL")
public class EliteMonsterKillEventDto extends EventsTimeLineDto {
    private Integer bounty;
    private Integer killerId;
    private Integer killerTeamId;
    private String monsterSubType;
    private String monsterType;
    private PositionDto position;
}
