package song.mygg1.domain.riot.repository.mastery;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import song.mygg1.domain.riot.entity.champion.ChampionMastery;
import song.mygg1.domain.riot.entity.champion.ChampionMasteryId;

import java.util.List;

@Repository
public interface ChampionMasteryJpaRepository extends JpaRepository<ChampionMastery, ChampionMasteryId> {
    @Query("select cm " +
            " from ChampionMastery cm " +
            "where cm.championMasteryId.puuid = :puuid " +
            " order by cm.championLevel desc")
    List<ChampionMastery> findChampionMasteryByPuuidInTop3(@Param("puuid") String puuid);
}
