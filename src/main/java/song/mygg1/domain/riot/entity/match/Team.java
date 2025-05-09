package song.mygg1.domain.riot.entity.match;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString(exclude = "info")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {
//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
    @EmbeddedId
    private TeamId id;

    @MapsId("infoId")
    @JoinColumn(name = "info_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Info info;

    private boolean win;

    public static Team create(Integer teamId, boolean win, Info info) {
        return new Team(teamId, win, info);
    }

    private Team(Integer teamId, boolean win, Info info) {
        this.id = new TeamId(info.getGameId(), teamId);
        this.win = win;
        this.info = info;
    }
}
