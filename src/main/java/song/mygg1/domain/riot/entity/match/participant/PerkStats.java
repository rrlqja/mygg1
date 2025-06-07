package song.mygg1.domain.riot.entity.match.participant;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class PerkStats {
    private Integer defense;
    private Integer flex;
    private Integer offense;
}
