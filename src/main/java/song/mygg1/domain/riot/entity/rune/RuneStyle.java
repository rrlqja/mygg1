package song.mygg1.domain.riot.entity.rune;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
public class RuneStyle {
    @Id
    private Integer id;

    @Column(name = "rune_style_key")
    private String key;
    private String name;
    private String icon;

    @OneToMany(mappedBy = "runeStyle", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RuneSlot> slots = new ArrayList<>();

    public void addRuneSlot(RuneSlot slot) {
        this.slots.add(slot);
        slot.setRuneStyle(this);
    }
}
