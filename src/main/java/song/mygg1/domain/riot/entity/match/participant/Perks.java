package song.mygg1.domain.riot.entity.match.participant;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import song.mygg1.domain.riot.entity.match.Participant;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Perks {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "perks")
    private Participant participant;

    @Embedded
    private PerkStats statPerks;

    @OneToMany(mappedBy = "perks", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PerkStyle> styles = new ArrayList<>();

    public void addStyle(PerkStyle style) {
        this.styles.add(style);
        style.setPerks(this);
    }
}
