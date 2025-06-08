package song.mygg1.domain.riot.repository.rune;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import song.mygg1.domain.riot.entity.rune.RuneStyle;

@Repository
public interface RuneStyleJpaRepository extends JpaRepository<RuneStyle, Integer> {
}
