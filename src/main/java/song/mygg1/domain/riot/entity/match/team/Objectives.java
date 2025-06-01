package song.mygg1.domain.riot.entity.match.team;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Objectives {
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "first", column = @Column(name = "baron_first")),
            @AttributeOverride(name = "kills", column = @Column(name = "baron_kills"))
    })
    private Objective baron;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "first", column = @Column(name = "champion_first")),
            @AttributeOverride(name = "kills", column = @Column(name = "champion_kills"))
    })
    private Objective champion;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "first", column = @Column(name = "dragon_first")),
            @AttributeOverride(name = "kills", column = @Column(name = "dragon_kills"))
    })
    private Objective dragon;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "first", column = @Column(name = "horde_first")),
            @AttributeOverride(name = "kills", column = @Column(name = "horde_kills"))
    })
    private Objective horde;


    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "first", column = @Column(name = "inhibitor_first")),
            @AttributeOverride(name = "kills", column = @Column(name = "inhibitor_kills"))
    })
    private Objective inhibitor;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "first", column = @Column(name = "riftHerald_first")),
            @AttributeOverride(name = "kills", column = @Column(name = "riftHerald_kills"))
    })
    private Objective riftHerald;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "first", column = @Column(name = "tower_first")),
            @AttributeOverride(name = "kills", column = @Column(name = "tower_kills"))
    })
    private Objective tower;
}
