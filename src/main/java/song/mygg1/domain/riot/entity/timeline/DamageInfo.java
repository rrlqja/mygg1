package song.mygg1.domain.riot.entity.timeline;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import song.mygg1.domain.riot.dto.timeline.DamageInfoDto;

@Getter @Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class DamageInfo {
    private Boolean basic;
    private Integer magicDamage;
    private String name;
    private Integer participantId;
    private Integer physicalDamage;
    private String spellName;
    private Integer spellSlot;
    private Integer trueDamage;
    private String type;

    public DamageInfo(DamageInfoDto damageInfoDto) {
        this.basic = damageInfoDto.getBasic();
        this.magicDamage = damageInfoDto.getMagicDamage();
        this.name = damageInfoDto.getName();
        this.participantId = damageInfoDto.getParticipantId();
        this.physicalDamage = damageInfoDto.getPhysicalDamage();
        this.spellName = damageInfoDto.getSpellName();
        this.spellSlot = damageInfoDto.getSpellSlot();
        this.trueDamage = damageInfoDto.getTrueDamage();
        this.type = damageInfoDto.getType();
    }
}
