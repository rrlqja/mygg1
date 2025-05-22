package song.mygg1.domain.riot.dto.timeline;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import song.mygg1.domain.riot.entity.timeline.ChampionStats;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChampionStatsDto {
    private Integer abilityHaste;
    private Integer abilityPower;
    private Integer armor;
    private Integer armorPen;
    private Integer armorPenPercent;
    private Integer attackDamage;
    private Integer attackSpeed;
    private Integer bonusArmorPenPercent;
    private Integer bonusMagicPenPercent;
    private Integer ccReduction;
    private Integer cooldownReduction;
    private Integer health;
    private Integer healthMax;
    private Integer healthRegen;
    private Integer lifesteal;
    private Integer magicPen;
    private Integer magicPenPercent;
    private Integer magicResist;
    private Integer movementSpeed;
    private Integer omnivamp;
    private Integer physicalVamp;
    private Integer power;
    private Integer powerMax;
    private Integer powerRegen;
    private Integer spellVamp;

    public ChampionStatsDto(ChampionStats championStats) {
        this.abilityHaste = championStats.getAbilityHaste();
        this.abilityPower = championStats.getAbilityPower();
        this.armor = championStats.getArmor();
        this.armorPen = championStats.getArmorPen();
        this.armorPenPercent = championStats.getArmorPenPercent();
        this.attackDamage = championStats.getAttackDamage();
        this.attackSpeed = championStats.getAttackSpeed();
        this.bonusArmorPenPercent = championStats.getBonusArmorPenPercent();
        this.bonusMagicPenPercent = championStats.getBonusMagicPenPercent();
        this.ccReduction = championStats.getCcReduction();
        this.cooldownReduction = championStats.getCooldownReduction();
        this.health = championStats.getHealth();
        this.healthMax = championStats.getHealthMax();
        this.healthRegen = championStats.getHealthRegen();
        this.lifesteal = championStats.getLifesteal();
        this.magicPen = championStats.getMagicPen();
        this.magicPenPercent = championStats.getMagicPenPercent();
        this.magicResist = championStats.getMagicResist();
        this.movementSpeed = championStats.getMovementSpeed();
        this.omnivamp = championStats.getOmnivamp();
        this.physicalVamp = championStats.getPhysicalVamp();
        this.power = championStats.getPower();
        this.powerMax = championStats.getPowerMax();
        this.powerRegen = championStats.getPowerRegen();
        this.spellVamp = championStats.getSpellVamp();
    }
}
