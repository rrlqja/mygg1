package song.mygg1.domain.riot.entity.league;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeagueEntryId implements Serializable {
    private String queueType;
    private String puuid;
}
