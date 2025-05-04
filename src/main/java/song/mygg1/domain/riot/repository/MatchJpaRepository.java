package song.mygg1.domain.riot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import song.mygg1.domain.riot.entity.match.Matches;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface MatchJpaRepository extends JpaRepository<Matches, Long> {
    @Query("select m " +
            " from Matches m " +
            " join fetch m.info " +
            " join fetch m.metadata " +
            "where m.matchId in :matchIds")
    List<Matches> findMatchesByMatchIdIn(@Param("matchIds") Collection<String> matchIds);

    @Query("select m " +
            " from Matches m " +
            " join fetch m.info " +
            " join fetch m.metadata " +
            "where m.matchId = :matchId")
    Optional<Matches> findMatchesByMatchId(@Param("matchId") String matchId);
}
