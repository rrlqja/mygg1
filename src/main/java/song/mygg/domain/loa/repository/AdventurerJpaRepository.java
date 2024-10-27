package song.mygg.domain.loa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import song.mygg.domain.loa.entity.Adventurer;

import java.util.Optional;

@Repository
public interface AdventurerJpaRepository extends JpaRepository<Adventurer, Long> {

    @Query("select a from Adventurer a where a.name = :name")
    Optional<Adventurer> findByName(@Param("name") String name);

}
