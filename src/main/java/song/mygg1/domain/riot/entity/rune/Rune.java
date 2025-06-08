package song.mygg1.domain.riot.entity.rune;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Rune {
    @Id
    private Integer id;

    @Column(name = "rune_key")
    private String key;
    private String name;
    private String icon;
    @Column(length = 1024)
    private String shortDesc;

    @Column(length = 2048, columnDefinition = "TEXT")
    private String longDesc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rune_slot_id")
    private RuneSlot runeSlot;
}
