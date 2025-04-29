package song.mygg1.domain.riot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import song.mygg1.domain.riot.entity.league.LeagueEntry;
import song.mygg1.domain.riot.entity.league.LeagueEntryId;

import java.util.Set;

@Repository
public interface LeagueEntryJpaRepository extends JpaRepository<LeagueEntry, LeagueEntryId> {
    @Query("select le " +
            " from LeagueEntry le " +
            "where le.id.puuid = :puuid")
    Set<LeagueEntry> findLeagueEntriesById_Puuid(@Param("puuid") String puuid);
}
