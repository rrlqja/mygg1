package song.mygg1.domain.riot.repository.champion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import song.mygg1.domain.riot.entity.champion.Champion;

import java.util.Optional;

@Repository
public interface ChampionJpaRepository extends JpaRepository<Champion, Long> {
    @Query("select c " +
            " from Champion c " +
            "where c.key = :key ")
    Optional<Champion> findChampionByKey(@Param("key") Long key);
}
