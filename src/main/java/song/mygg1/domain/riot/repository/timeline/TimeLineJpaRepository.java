package song.mygg1.domain.riot.repository.timeline;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import song.mygg1.domain.riot.entity.timeline.Timeline;

import java.util.Optional;

@Repository
public interface TimeLineJpaRepository extends JpaRepository<Timeline, String> {
    @Query("select tm " +
            " from Timeline tm " +
            "where tm.matchId = :matchId")
    Optional<Timeline> findTimelineByMatchId(@Param("matchId") String matchId);
}
