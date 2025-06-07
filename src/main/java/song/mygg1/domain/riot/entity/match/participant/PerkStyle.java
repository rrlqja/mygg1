package song.mygg1.domain.riot.entity.match.participant;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PerkStyle {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "perks_id")
    private Perks perks;

    private String description;
    private Integer style;

    @OneToMany(mappedBy = "perkStyle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PerkStyleSelection> selections = new ArrayList<>();

    public void addSelection(PerkStyleSelection selection) {
        this.selections.add(selection);
        selection.setPerkStyle(this);
    }
}
