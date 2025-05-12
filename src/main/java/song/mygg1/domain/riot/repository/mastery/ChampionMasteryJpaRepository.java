package song.mygg1.domain.riot.repository.mastery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import song.mygg1.domain.riot.entity.champion.ChampionMastery;
import song.mygg1.domain.riot.entity.champion.ChampionMasteryId;

@Repository
public interface ChampionMasteryJpaRepository extends JpaRepository<ChampionMastery, ChampionMasteryId> {
    @Query("select cm " +
            " from ChampionMastery cm " +
            "where cm.championMasteryId.puuid = :puuid " +
            "order by cm.championPoints desc")
    Page<ChampionMastery> findChampionMasteriesByPuuid(@Param("puuid") String puuid,
                                                       Pageable pageable);
}
