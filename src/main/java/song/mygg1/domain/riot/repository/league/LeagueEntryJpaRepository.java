package song.mygg1.domain.riot.repository.league;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
            "where le.id.puuid = :puuid " +
            "  and le.deleteFlag = false")
    Set<LeagueEntry> findLeagueEntriesById_Puuid(@Param("puuid") String puuid);

    @Query("select le " +
            " from LeagueEntry le " +
            "where le.tier in ('CHALLENGER', 'GRANDMASTER')" +
            "order by le.leaguePoints desc")
    Page<LeagueEntry> findLeagueEntriesInRanker(Pageable pageable);
}
