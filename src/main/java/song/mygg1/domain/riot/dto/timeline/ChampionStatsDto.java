package song.mygg1.domain.riot.dto.timeline;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
