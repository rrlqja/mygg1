package song.mygg1.domain.riot.dto.timeline;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import song.mygg1.domain.riot.entity.timeline.DamageInfo;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DamageInfoDto {
    private Boolean basic;
    private Integer magicDamage;
    private String name;
    private Integer participantId;
    private Integer physicalDamage;
    private String spellName;
    private Integer spellSlot;
    private Integer trueDamage;
    private String type;

    public DamageInfoDto(DamageInfo damageInfo) {
        this.basic = damageInfo.getBasic();
        this.magicDamage = damageInfo.getMagicDamage();
        this.name = damageInfo.getName();
        this.participantId = damageInfo.getParticipantId();
        this.physicalDamage = damageInfo.getPhysicalDamage();
        this.spellName = damageInfo.getSpellName();
        this.spellSlot = damageInfo.getSpellSlot();
        this.trueDamage = damageInfo.getTrueDamage();
        this.type = damageInfo.getType();
    }
}
