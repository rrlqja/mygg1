package song.mygg1.domain.riot.entity.match.team;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Ban {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer championId;
    private Integer pickTurn;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "info_id", referencedColumnName = "info_id"),
            @JoinColumn(name = "team_id", referencedColumnName = "team_id")
    })
    private Team team;

    public static Ban create(Integer championId, Integer pickTurn, Team team) {
        return new Ban(championId, pickTurn, team);
    }

    private Ban(Integer championId, Integer pickTurn, Team team) {
        this.championId = championId;
        this.pickTurn = pickTurn;
        this.team = team;
    }
}
