package song.mygg1.domain.riot.entity.league;

import jakarta.persistence.CascadeType;
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
public class LeagueList {
    @Id
    private String leagueId;

    private String tier;
    private String queue;
    private String name;

    @OneToMany(mappedBy = "leagueList", fetch = FetchType.LAZY, cascade = CascadeType.MERGE, orphanRemoval = true)
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

    public void addEntry(LeagueItem item) {
        this.entries.add(item);
    }
}
