package song.mygg1.domain.riot.repository.league;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import song.mygg1.domain.riot.entity.league.LeagueList;

import java.util.Optional;

@Repository
public interface LeagueListJpaRepository extends JpaRepository<LeagueList, String> {
    @Query("select ll " +
            " from LeagueList ll " +
            " join fetch ll.entries " +
            "where ll.leagueId = :leagueId")
    Optional<LeagueList> findByLeagueId(@Param("leagueId") String leagueId);

    Optional<LeagueList> findByTierAndQueue(String tier, String queue);
}
