package song.mygg1.domain.riot.mapper.timeline;

import org.springframework.stereotype.Component;
import song.mygg1.domain.riot.dto.timeline.ChampionStatsDto;
import song.mygg1.domain.riot.dto.timeline.DamageStatsDto;
import song.mygg1.domain.riot.dto.timeline.ParticipantFrameDto;
import song.mygg1.domain.riot.dto.timeline.PositionDto;
import song.mygg1.domain.riot.entity.timeline.ChampionStats;
import song.mygg1.domain.riot.entity.timeline.DamageStats;
import song.mygg1.domain.riot.entity.timeline.FrameTimeLine;
import song.mygg1.domain.riot.entity.timeline.FrameTimeLineId;
import song.mygg1.domain.riot.entity.timeline.ParticipantFrame;
import song.mygg1.domain.riot.entity.timeline.ParticipantFrameId;
import song.mygg1.domain.riot.entity.timeline.Position;

@Component
public class ParticipantFrameMapper {
    public ParticipantFrameDto toDto(ParticipantFrame entity) {
        ParticipantFrameDto dto = new ParticipantFrameDto();

        dto.setParticipantId(entity.getId().getParticipantId());
        dto.setChampionStats(new ChampionStatsDto(entity.getChampionStats()));
        dto.setCurrentGold(entity.getCurrentGold());
        dto.setDamageStats(new DamageStatsDto(entity.getDamageStats()));
        dto.setGoldPerSecond(entity.getGoldPerSecond());
        dto.setJungleMinionsKilled(entity.getJungleMinionsKilled());
        dto.setLevel(entity.getLevel());
        dto.setMinionsKilled(entity.getMinionsKilled());
        dto.setPosition(new PositionDto(entity.getPosition()));
        dto.setTimeEnemySpentControlled(entity.getTimeEnemySpentControlled());
        dto.setTotalGold(entity.getTotalGold());
        dto.setXp(entity.getXp());

        return dto;
    }

    public ParticipantFrame toEntity(ParticipantFrameDto dto, FrameTimeLine frame) {
        ParticipantFrame entity = new ParticipantFrame();

        entity.setId(new ParticipantFrameId(new FrameTimeLineId(frame.getId().getMatchId(), frame.getId().getTimestamp()), dto.getParticipantId()));
        entity.setCurrentGold(dto.getCurrentGold());
        entity.setGoldPerSecond(dto.getGoldPerSecond());
        entity.setJungleMinionsKilled(dto.getJungleMinionsKilled());
        entity.setLevel(dto.getLevel());
        entity.setMinionsKilled(dto.getMinionsKilled());
        entity.setTimeEnemySpentControlled(dto.getTimeEnemySpentControlled());
        entity.setTotalGold(dto.getTotalGold());
        entity.setXp(dto.getXp());

        entity.setChampionStats(new ChampionStats(dto.getChampionStats()));
        entity.setDamageStats(new DamageStats(dto.getDamageStats()));
        entity.setPosition(new Position(dto.getPosition()));

        entity.setFrame(frame);

        return entity;
    }
}
