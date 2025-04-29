package song.mygg1.domain.riot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "info_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Info info;

    private Integer teamId;
    private boolean win;

    public static Team create(Integer teamId, boolean win, Info info) {
        return new Team(teamId, win, info);
    }

    private Team(Integer teamId, boolean win, Info info) {
        this.teamId = teamId;
        this.win = win;
        this.info = info;
    }
}
