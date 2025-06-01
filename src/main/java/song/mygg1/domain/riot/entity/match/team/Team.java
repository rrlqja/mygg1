package song.mygg1.domain.riot.entity.match.team;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import song.mygg1.domain.riot.entity.match.Info;

import java.util.ArrayList;
import java.util.List;

@ToString(exclude = {"info", "bans"})
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {
    @EmbeddedId
    private TeamId id;

    @JsonIgnore
    @MapsId("infoId")
    @JoinColumn(name = "info_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Info info;

    private boolean win;

    @Embedded
    private Objectives objectives;

    @Setter
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ban> bans = new ArrayList<>();

    public static Team create(Integer teamId, boolean win, Info info, Objectives objectives) {
        return new Team(teamId, win, info, objectives);
    }

    private Team(Integer teamId, boolean win, Info info, Objectives objectives) {
        this.id = new TeamId(info.getGameId(), teamId);
        this.win = win;
        this.info = info;
        this.objectives = objectives;
    }
}
