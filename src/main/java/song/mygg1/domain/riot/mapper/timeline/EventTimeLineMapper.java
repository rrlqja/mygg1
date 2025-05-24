package song.mygg1.domain.riot.mapper.timeline;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import song.mygg1.domain.riot.dto.timeline.DamageInfoDto;
import song.mygg1.domain.riot.dto.timeline.EventsTimeLineDto;
import song.mygg1.domain.riot.dto.timeline.PositionDto;
import song.mygg1.domain.riot.dto.timeline.event.BuildingKillEventDto;
import song.mygg1.domain.riot.dto.timeline.event.ChampionKillEventDto;
import song.mygg1.domain.riot.dto.timeline.event.ChampionSpecialKillEventDto;
import song.mygg1.domain.riot.dto.timeline.event.ChampionTransformEventDto;
import song.mygg1.domain.riot.dto.timeline.event.CommonEventDto;
import song.mygg1.domain.riot.dto.timeline.event.DragonSoulGivenEventDto;
import song.mygg1.domain.riot.dto.timeline.event.EliteMonsterKillEventDto;
import song.mygg1.domain.riot.dto.timeline.event.FeatUpdateEventDto;
import song.mygg1.domain.riot.dto.timeline.event.GameEndEventDto;
import song.mygg1.domain.riot.dto.timeline.event.ItemDestroyEventDto;
import song.mygg1.domain.riot.dto.timeline.event.ItemPurchaseEventDto;
import song.mygg1.domain.riot.dto.timeline.event.ItemSoldEventDto;
import song.mygg1.domain.riot.dto.timeline.event.ItemUndoEventDto;
import song.mygg1.domain.riot.dto.timeline.event.LevelUpEventDto;
import song.mygg1.domain.riot.dto.timeline.event.ObjectBountyFinishEventDto;
import song.mygg1.domain.riot.dto.timeline.event.ObjectBountyPrestartEventDto;
import song.mygg1.domain.riot.dto.timeline.event.PauseEndEventDto;
import song.mygg1.domain.riot.dto.timeline.event.SkillLevelUpEventDto;
import song.mygg1.domain.riot.dto.timeline.event.TurretPlateDestroyEventDto;
import song.mygg1.domain.riot.dto.timeline.event.WardKillEventDto;
import song.mygg1.domain.riot.dto.timeline.event.WardPlaceEventDto;
import song.mygg1.domain.riot.entity.timeline.DamageInfo;
import song.mygg1.domain.riot.entity.timeline.EventTimeLine;
import song.mygg1.domain.riot.entity.timeline.FrameTimeLine;
import song.mygg1.domain.riot.entity.timeline.Position;
import song.mygg1.domain.riot.entity.timeline.events.BuildingKillEvent;
import song.mygg1.domain.riot.entity.timeline.events.ChampionKillEvent;
import song.mygg1.domain.riot.entity.timeline.events.ChampionSpecialKillEvent;
import song.mygg1.domain.riot.entity.timeline.events.ChampionTransformEvent;
import song.mygg1.domain.riot.entity.timeline.events.CommonEvent;
import song.mygg1.domain.riot.entity.timeline.events.DragonSoulGivenEvent;
import song.mygg1.domain.riot.entity.timeline.events.EliteMonsterKillEvent;
import song.mygg1.domain.riot.entity.timeline.events.FeatUpdateEvent;
import song.mygg1.domain.riot.entity.timeline.events.GameEndEvent;
import song.mygg1.domain.riot.entity.timeline.events.ItemDestroyEvent;
import song.mygg1.domain.riot.entity.timeline.events.ItemPurchaseEvent;
import song.mygg1.domain.riot.entity.timeline.events.ItemSoldEvent;
import song.mygg1.domain.riot.entity.timeline.events.ItemUndoEvent;
import song.mygg1.domain.riot.entity.timeline.events.LevelUpEvent;
import song.mygg1.domain.riot.entity.timeline.events.ObjectBountyFinishEvent;
import song.mygg1.domain.riot.entity.timeline.events.ObjectBountyPrestartEvent;
import song.mygg1.domain.riot.entity.timeline.events.PauseEndEvent;
import song.mygg1.domain.riot.entity.timeline.events.SkillLevelUpEvent;
import song.mygg1.domain.riot.entity.timeline.events.TurretPlateDestroyEvent;
import song.mygg1.domain.riot.entity.timeline.events.WardKillEvent;
import song.mygg1.domain.riot.entity.timeline.events.WardPlaceEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventTimeLineMapper {
    public EventsTimeLineDto toDto(EventTimeLine entity) {
        EventsTimeLineDto dto;

        if (entity instanceof PauseEndEvent pauseEndEvent) {
            PauseEndEventDto subDto = new PauseEndEventDto();

            subDto.setRealTimestamp(pauseEndEvent.getRealTimestamp());

            dto = subDto;
        } else if (entity instanceof ItemPurchaseEvent itemPurchaseEvent) {
            ItemPurchaseEventDto subDto = new ItemPurchaseEventDto();

            subDto.setItemId(itemPurchaseEvent.getItemId());
            subDto.setParticipantId(itemPurchaseEvent.getParticipantId());

            dto = subDto;
        } else if (entity instanceof ItemSoldEvent itemSoldEvent) {
            ItemSoldEventDto subDto = new ItemSoldEventDto();

            subDto.setItemId(itemSoldEvent.getItemId());
            subDto.setParticipantId(itemSoldEvent.getParticipantId());

            dto = subDto;
        } else if (entity instanceof SkillLevelUpEvent skillLevelUpEvent) {
            SkillLevelUpEventDto subDto = new SkillLevelUpEventDto();

            subDto.setSkillSlot(skillLevelUpEvent.getSkillSlot());
            subDto.setParticipantId(skillLevelUpEvent.getParticipantId());
            subDto.setLevelUpType(skillLevelUpEvent.getLevelUpType());

            dto = subDto;
        } else if (entity instanceof ChampionKillEvent championKillEvent) {
            ChampionKillEventDto subDto = new ChampionKillEventDto();

            subDto.setAssistingParticipantIds(championKillEvent.getAssistingParticipantIds());
            subDto.setBounty(championKillEvent.getBounty());
            subDto.setKillStreakLength(championKillEvent.getKillStreakLength());
            subDto.setKillerId(championKillEvent.getKillerId());
            subDto.setPosition(new PositionDto(championKillEvent.getPosition()));
            subDto.setShutdownBounty(championKillEvent.getShutdownBounty());
            subDto.setVictimDamageDealt(championKillEvent.getVictimDamageDealt().stream().map(DamageInfoDto::new).toList());
            subDto.setVictimDamageReceived(championKillEvent.getVictimDamageReceived().stream().map(DamageInfoDto::new).toList());
            subDto.setVictimId(championKillEvent.getVictimId());

            dto = subDto;
        } else if (entity instanceof BuildingKillEvent buildingKillEvent) {
            BuildingKillEventDto subDto = new BuildingKillEventDto();

            subDto.setAssistingParticipantIds(buildingKillEvent.getAssistingParticipantIds());
            subDto.setBounty(buildingKillEvent.getBounty());
            subDto.setBuildingType(buildingKillEvent.getBuildingType());
            subDto.setKillerId(buildingKillEvent.getKillerId());
            subDto.setLaneType(buildingKillEvent.getLaneType());
            subDto.setPosition(new PositionDto(buildingKillEvent.getPosition()));
            subDto.setTeamId(buildingKillEvent.getTeamId());
            subDto.setTowerType(buildingKillEvent.getTowerType());

            dto = subDto;
        } else if (entity instanceof LevelUpEvent levelUpEvent) {
            LevelUpEventDto subDto = new LevelUpEventDto();

            subDto.setLevel(levelUpEvent.getLevel());
            subDto.setParticipantId(levelUpEvent.getParticipantId());

            dto = subDto;
        } else if (entity instanceof WardPlaceEvent wardPlaceEvent) {
            WardPlaceEventDto subDto = new WardPlaceEventDto();

            subDto.setCreatorId(wardPlaceEvent.getCreatorId());
            subDto.setWardType(wardPlaceEvent.getWardType());

            dto = subDto;
        } else if (entity instanceof WardKillEvent wardKillEvent) {
            WardKillEventDto subDto = new WardKillEventDto();

            subDto.setKillerId(wardKillEvent.getKillerId());
            subDto.setWardType(wardKillEvent.getWardType());

            dto = subDto;
        } else if (entity instanceof ChampionSpecialKillEvent championSpecialKillEvent) {
            ChampionSpecialKillEventDto subDto = new ChampionSpecialKillEventDto();

            subDto.setKillType(championSpecialKillEvent.getKillType());
            subDto.setKillId(championSpecialKillEvent.getKillId());
            subDto.setPosition(new PositionDto(championSpecialKillEvent.getPosition()));

            dto = subDto;
        } else if (entity instanceof ChampionTransformEvent championTransformEvent) {
            ChampionTransformEventDto subDto = new ChampionTransformEventDto();

            subDto.setParticipantId(championTransformEvent.getParticipantId());
            subDto.setTransformType(championTransformEvent.getTransformType());

            dto = subDto;
        } else if (entity instanceof ItemDestroyEvent itemDestroyEvent) {
            ItemDestroyEventDto subDto = new ItemDestroyEventDto();

            subDto.setItemId(itemDestroyEvent.getItemId());
            subDto.setParticipantId(itemDestroyEvent.getParticipantId());

            dto = subDto;
        } else if (entity instanceof ItemUndoEvent itemUndoEvent) {
            ItemUndoEventDto subDto = new ItemUndoEventDto();

            subDto.setAfterId(itemUndoEvent.getAfterId());
            subDto.setBeforeId(itemUndoEvent.getBeforeId());
            subDto.setGoldGain(itemUndoEvent.getGoldGain());
            subDto.setParticipantId(itemUndoEvent.getParticipantId());

            dto = subDto;
        } else if (entity instanceof FeatUpdateEvent featUpdateEvent) {
            FeatUpdateEventDto subDto = new FeatUpdateEventDto();

            subDto.setFeatType(featUpdateEvent.getFeatType());
            subDto.setFeatValue(featUpdateEvent.getFeatValue());
            subDto.setTeamId(featUpdateEvent.getTeamId());

            dto = subDto;
        } else if (entity instanceof TurretPlateDestroyEvent turretPlateDestroyEvent) {
            TurretPlateDestroyEventDto subDto = new TurretPlateDestroyEventDto();

            subDto.setKillerId(turretPlateDestroyEvent.getKillerId());
            subDto.setLaneType(turretPlateDestroyEvent.getLaneType());
            subDto.setPosition(new PositionDto(turretPlateDestroyEvent.getPosition()));
            subDto.setTeamId(turretPlateDestroyEvent.getTeamId());

            dto = subDto;
        } else if (entity instanceof EliteMonsterKillEvent eliteMonsterKillEvent) {
            EliteMonsterKillEventDto subDto = new EliteMonsterKillEventDto();

            subDto.setBounty(eliteMonsterKillEvent.getBounty());
            subDto.setKillerId(eliteMonsterKillEvent.getKillerId());
            subDto.setKillerTeamId(eliteMonsterKillEvent.getKillerTeamId());
            subDto.setMonsterSubType(eliteMonsterKillEvent.getMonsterSubType());
            subDto.setMonsterType(eliteMonsterKillEvent.getMonsterType());
            subDto.setPosition(new PositionDto(eliteMonsterKillEvent.getPosition()));

            dto = subDto;
        } else if (entity instanceof DragonSoulGivenEvent dragonSoulGivenEvent) {
            DragonSoulGivenEventDto subDto = new DragonSoulGivenEventDto();

            subDto.setName(dragonSoulGivenEvent.getName());
            subDto.setTeamId(dragonSoulGivenEvent.getTeamId());

            dto = subDto;
        } else if (entity instanceof ObjectBountyPrestartEvent objectBountyPrestartEvent) {
            ObjectBountyPrestartEventDto subDto = new ObjectBountyPrestartEventDto();

            subDto.setActualStartTime(objectBountyPrestartEvent.getActualStartTime());
            subDto.setTeamId(objectBountyPrestartEvent.getTeamId());

            dto = subDto;
        } else if (entity instanceof ObjectBountyFinishEvent objectBountyFinishEvent) {
            ObjectBountyFinishEventDto subDto = new ObjectBountyFinishEventDto();

            subDto.setTeamId(objectBountyFinishEvent.getTeamId());

            dto = subDto;
        } else if (entity instanceof GameEndEvent gameEndEvent) {
            GameEndEventDto subDto = new GameEndEventDto();

            subDto.setGameId(gameEndEvent.getGameId());
            subDto.setRealTimestamp(gameEndEvent.getRealTimestamp());
            subDto.setWinningTeam(gameEndEvent.getWinningTeam());

            dto = subDto;
        } else {
            dto = new CommonEventDto();
        }

        dto.setTimestamp(entity.getTimestamp());
        dto.setType(entity.getType());

        return dto;
    }

    public EventTimeLine toEntity(EventsTimeLineDto dto, FrameTimeLine frame) {
        EventTimeLine entity;

        if (dto instanceof PauseEndEventDto pauseEndEvent) {
            PauseEndEvent subEntity = new PauseEndEvent();

            subEntity.setRealTimestamp(pauseEndEvent.getRealTimestamp());

            entity = subEntity;
        } else if (dto instanceof ItemPurchaseEventDto itemPurchaseEvent) {
            ItemPurchaseEvent subEntity = new ItemPurchaseEvent();

            subEntity.setItemId(itemPurchaseEvent.getItemId());
            subEntity.setParticipantId(itemPurchaseEvent.getParticipantId());

            entity = subEntity;
        } else if (dto instanceof ItemSoldEventDto itemSoldEvent) {
            ItemSoldEvent subEntity = new ItemSoldEvent();

            subEntity.setItemId(itemSoldEvent.getItemId());
            subEntity.setParticipantId(itemSoldEvent.getParticipantId());

            entity = subEntity;
        } else if (dto instanceof SkillLevelUpEventDto skillLevelUpEvent) {
            SkillLevelUpEvent subEntity = new SkillLevelUpEvent();

            subEntity.setSkillSlot(skillLevelUpEvent.getSkillSlot());
            subEntity.setParticipantId(skillLevelUpEvent.getParticipantId());
            subEntity.setLevelUpType(skillLevelUpEvent.getLevelUpType());

            entity = subEntity;
        } else if (dto instanceof ChampionKillEventDto championKillEvent) {
            ChampionKillEvent subEntity = new ChampionKillEvent();

            subEntity.setAssistingParticipantIds(championKillEvent.getAssistingParticipantIds());
            subEntity.setBounty(championKillEvent.getBounty());
            subEntity.setKillStreakLength(championKillEvent.getKillStreakLength());
            subEntity.setKillerId(championKillEvent.getKillerId());
            subEntity.setPosition(new Position(championKillEvent.getPosition()));
            subEntity.setShutdownBounty(championKillEvent.getShutdownBounty());
            subEntity.setVictimDamageDealt(championKillEvent.getVictimDamageDealt().stream().map(DamageInfo::new).toList());
            subEntity.setVictimDamageReceived(championKillEvent.getVictimDamageReceived().stream().map(DamageInfo::new).toList());
            subEntity.setVictimId(championKillEvent.getVictimId());

            entity = subEntity;
        } else if (dto instanceof BuildingKillEventDto buildingKillEvent) {
            BuildingKillEvent subEntity = new BuildingKillEvent();

            subEntity.setAssistingParticipantIds(buildingKillEvent.getAssistingParticipantIds());
            subEntity.setBounty(buildingKillEvent.getBounty());
            subEntity.setBuildingType(buildingKillEvent.getBuildingType());
            subEntity.setKillerId(buildingKillEvent.getKillerId());
            subEntity.setLaneType(buildingKillEvent.getLaneType());
            subEntity.setPosition(new Position(buildingKillEvent.getPosition()));
            subEntity.setTeamId(buildingKillEvent.getTeamId());
            subEntity.setTowerType(buildingKillEvent.getTowerType());

            entity = subEntity;
        } else if (dto instanceof LevelUpEventDto levelUpEvent) {
            LevelUpEvent subEntity = new LevelUpEvent();

            subEntity.setLevel(levelUpEvent.getLevel());
            subEntity.setParticipantId(levelUpEvent.getParticipantId());

            entity = subEntity;
        } else if (dto instanceof WardPlaceEventDto wardPlaceEvent) {
            WardPlaceEvent subEntity = new WardPlaceEvent();

            subEntity.setCreatorId(wardPlaceEvent.getCreatorId());
            subEntity.setWardType(wardPlaceEvent.getWardType());

            entity = subEntity;
        } else if (dto instanceof WardKillEventDto wardKillEvent) {
            WardKillEvent subEntity = new WardKillEvent();

            subEntity.setKillerId(wardKillEvent.getKillerId());
            subEntity.setWardType(wardKillEvent.getWardType());

            entity = subEntity;
        } else if (dto instanceof ChampionSpecialKillEventDto championSpecialKillEvent) {
            ChampionSpecialKillEvent subEntity = new ChampionSpecialKillEvent();

            subEntity.setKillType(championSpecialKillEvent.getKillType());
            subEntity.setKillId(championSpecialKillEvent.getKillId());
            subEntity.setPosition(new Position(championSpecialKillEvent.getPosition()));

            entity = subEntity;
        } else if (dto instanceof ChampionTransformEventDto championTransformEvent) {
            ChampionTransformEvent subEntity = new ChampionTransformEvent();

            subEntity.setParticipantId(championTransformEvent.getParticipantId());
            subEntity.setTransformType(championTransformEvent.getTransformType());

            entity = subEntity;
        } else if (dto instanceof ItemDestroyEventDto itemDestroyEvent) {
            ItemPurchaseEvent subEntity = new ItemPurchaseEvent();

            subEntity.setItemId(itemDestroyEvent.getItemId());
            subEntity.setParticipantId(itemDestroyEvent.getParticipantId());

            entity = subEntity;
        } else if (dto instanceof ItemUndoEventDto itemUndoEvent) {
            ItemUndoEvent subEntity = new ItemUndoEvent();

            subEntity.setAfterId(itemUndoEvent.getAfterId());
            subEntity.setBeforeId(itemUndoEvent.getBeforeId());
            subEntity.setGoldGain(itemUndoEvent.getGoldGain());
            subEntity.setParticipantId(itemUndoEvent.getParticipantId());

            entity = subEntity;
        } else if (dto instanceof FeatUpdateEventDto featUpdateEvent) {
            FeatUpdateEvent subEntity = new FeatUpdateEvent();

            subEntity.setFeatType(featUpdateEvent.getFeatType());
            subEntity.setFeatValue(featUpdateEvent.getFeatValue());
            subEntity.setTeamId(featUpdateEvent.getTeamId());

            entity = subEntity;
        } else if (dto instanceof TurretPlateDestroyEventDto turretPlateDestroyEvent) {
            TurretPlateDestroyEvent subEntity = new TurretPlateDestroyEvent();

            subEntity.setKillerId(turretPlateDestroyEvent.getKillerId());
            subEntity.setLaneType(turretPlateDestroyEvent.getLaneType());
            subEntity.setPosition(new Position(turretPlateDestroyEvent.getPosition()));
            subEntity.setTeamId(turretPlateDestroyEvent.getTeamId());

            entity = subEntity;
        } else if (dto instanceof EliteMonsterKillEventDto eliteMonsterKillEvent) {
            EliteMonsterKillEvent subEntity = new EliteMonsterKillEvent();

            subEntity.setBounty(eliteMonsterKillEvent.getBounty());
            subEntity.setKillerId(eliteMonsterKillEvent.getKillerId());
            subEntity.setKillerTeamId(eliteMonsterKillEvent.getKillerTeamId());
            subEntity.setMonsterSubType(eliteMonsterKillEvent.getMonsterSubType());
            subEntity.setMonsterType(eliteMonsterKillEvent.getMonsterType());
            subEntity.setPosition(new Position(eliteMonsterKillEvent.getPosition()));

            entity = subEntity;
        } else if (dto instanceof DragonSoulGivenEventDto dragonSoulGivenEvent) {
            DragonSoulGivenEvent subEntity = new DragonSoulGivenEvent();

            subEntity.setName(dragonSoulGivenEvent.getName());
            subEntity.setTeamId(dragonSoulGivenEvent.getTeamId());

            entity = subEntity;
        } else if (dto instanceof ObjectBountyPrestartEventDto objectBountyPrestartEvent) {
            ObjectBountyPrestartEvent subEntity = new ObjectBountyPrestartEvent();

            subEntity.setActualStartTime(objectBountyPrestartEvent.getActualStartTime());
            subEntity.setTeamId(objectBountyPrestartEvent.getTeamId());

            entity = subEntity;
        } else if (dto instanceof ObjectBountyFinishEventDto objectBountyFinishEvent) {
            ObjectBountyFinishEvent subEntity = new ObjectBountyFinishEvent();

            subEntity.setTeamId(objectBountyFinishEvent.getTeamId());

            entity = subEntity;
        } else if (dto instanceof GameEndEventDto gameEndEvent) {
            GameEndEvent subEntity = new GameEndEvent();

            subEntity.setGameId(gameEndEvent.getGameId());
            subEntity.setRealTimestamp(gameEndEvent.getRealTimestamp());
            subEntity.setWinningTeam(gameEndEvent.getWinningTeam());

            entity = subEntity;
        } else {
            entity = new CommonEvent();
        }

        entity.setTimestamp(dto.getTimestamp());
        entity.setType(dto.getType());

        entity.setFrame(frame);

        return entity;
    }
}
