package song.mygg1.domain.riot.entity.timeline;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import song.mygg1.domain.riot.dto.timeline.ChampionStatsDto;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class ChampionStats {
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

    public ChampionStats(ChampionStatsDto dto) {
        this.abilityHaste = dto.getAbilityHaste();
        this.abilityPower = dto.getAbilityPower();
        this.armor = dto.getArmor();
        this.armorPen = dto.getArmorPen();
        this.armorPenPercent = dto.getArmorPenPercent();
        this.attackDamage = dto.getAttackDamage();
        this.attackSpeed = dto.getAttackSpeed();
        this.bonusArmorPenPercent = dto.getBonusArmorPenPercent();
        this.bonusMagicPenPercent = dto.getBonusMagicPenPercent();
        this.ccReduction = dto.getCcReduction();
        this.cooldownReduction = dto.getCooldownReduction();
        this.health = dto.getHealth();
        this.healthMax = dto.getHealthMax();
        this.healthRegen = dto.getHealthRegen();
        this.lifesteal = dto.getLifesteal();
        this.magicPen = dto.getMagicPen();
        this.magicPenPercent = dto.getMagicPenPercent();
        this.magicResist = dto.getMagicResist();
        this.movementSpeed = dto.getMovementSpeed();
        this.omnivamp = dto.getOmnivamp();
        this.physicalVamp = dto.getPhysicalVamp();
        this.power = dto.getPower();
        this.powerMax = dto.getPowerMax();
        this.powerRegen = dto.getPowerRegen();
        this.spellVamp = dto.getSpellVamp();
    }
}
