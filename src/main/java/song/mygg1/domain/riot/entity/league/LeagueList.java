package song.mygg1.domain.riot.entity.league;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LeagueList {
    @Id
    private String leagueId;

    private String tier;
    private String queue;
    private String name;

    @OneToMany(mappedBy = "leagueList", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LeagueItem> entries = new ArrayList<>();

    public static LeagueList create(String leagueId, String tier, String queue, String name) {
        return new LeagueList(leagueId, tier, queue, name);
    }

    private LeagueList(String leagueId, String tier, String queue, String name) {
        this.leagueId = leagueId;
        this.tier = tier;
        this.queue = queue;
        this.name = name;
    }
}
