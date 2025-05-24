package song.mygg1.domain.riot.dto.timeline;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
import song.mygg1.domain.riot.dto.timeline.event.ObjectBountyPrestartEventDto;
import song.mygg1.domain.riot.dto.timeline.event.PauseEndEventDto;
import song.mygg1.domain.riot.dto.timeline.event.SkillLevelUpEventDto;
import song.mygg1.domain.riot.dto.timeline.event.TurretPlateDestroyEventDto;
import song.mygg1.domain.riot.dto.timeline.event.WardKillEventDto;
import song.mygg1.domain.riot.dto.timeline.event.WardPlaceEventDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        visible = true,
        defaultImpl = CommonEventDto.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = PauseEndEventDto.class, name = "PAUSE_END"),
        @JsonSubTypes.Type(value = ItemPurchaseEventDto.class, name = "ITEM_PURCHASED"),
        @JsonSubTypes.Type(value = ItemSoldEventDto.class, name = "ITEM_SOLD"),
        @JsonSubTypes.Type(value = SkillLevelUpEventDto.class, name = "SKILL_LEVEL_UP"),
        @JsonSubTypes.Type(value = ChampionKillEventDto.class, name = "CHAMPION_KILL"),
        @JsonSubTypes.Type(value = LevelUpEventDto.class, name = "LEVEL_UP"),
        @JsonSubTypes.Type(value = WardPlaceEventDto.class, name = "WARD_PLACED"),
        @JsonSubTypes.Type(value = ChampionSpecialKillEventDto.class, name = "CHAMPION_SPECIAL_KILL"),
        @JsonSubTypes.Type(value = ChampionTransformEventDto.class, name = "CHAMPION_TRANSFORM"),
        @JsonSubTypes.Type(value = ItemDestroyEventDto.class, name = "ITEM_DESTROYED"),
        @JsonSubTypes.Type(value = ItemUndoEventDto.class, name = "ITEM_UNDO"),
        @JsonSubTypes.Type(value = WardKillEventDto.class, name = "WARD_KILL"),
        @JsonSubTypes.Type(value = BuildingKillEventDto.class, name = "BUILDING_KILL"),
        @JsonSubTypes.Type(value = FeatUpdateEventDto.class, name = "FEAT_UPDATE"),
        @JsonSubTypes.Type(value = TurretPlateDestroyEventDto.class, name = "TURRET_PLATE_DESTROYED"),
        @JsonSubTypes.Type(value = EliteMonsterKillEventDto.class, name = "ELITE_MONSTER_KILL"),
        @JsonSubTypes.Type(value = DragonSoulGivenEventDto.class, name = "DRAGON_SOUL_GIVEN"),
        @JsonSubTypes.Type(value = ObjectBountyPrestartEventDto.class, name = "OBJECTIVE_BOUNTY_PRESTART"),
        @JsonSubTypes.Type(value = ObjectBountyPrestartEventDto.class, name = "OBJECTIVE_BOUNTY_FINISH"),
        @JsonSubTypes.Type(value = GameEndEventDto.class, name = "GAME_END")
})
public abstract class EventsTimeLineDto {
    private Long timestamp;
    private String type;
}
