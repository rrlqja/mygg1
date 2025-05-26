package song.mygg1.domain.riot.repository.timeline;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import song.mygg1.domain.riot.entity.timeline.events.SkillLevelUpEvent;

import java.util.List;

@Repository
public interface SkillLevelUpEventJpaRepository extends JpaRepository<SkillLevelUpEvent, Long> {
    @Query("select slue " +
            " from SkillLevelUpEvent slue " +
            "where slue.frame.id.matchId = :matchId" +
            "  and slue.participantId = :participantId  " +
            "  and slue.levelUpType = 'NORMAL'" +
            "order by slue.timestamp asc")
    List<SkillLevelUpEvent> findByMatchIdsInAndLevelUpType(@Param("matchId") String matchId,
                                                           @Param("participantId") Integer participantId);
}
