package song.mygg1.domain.riot.repository.timeline;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import song.mygg1.domain.riot.entity.timeline.events.LevelUpEvent;

@Repository
public interface LevelUpEventJpaRepository extends JpaRepository<LevelUpEvent, Long> {

}
