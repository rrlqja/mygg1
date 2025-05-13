package song.mygg1.domain.riot.repository.champion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import song.mygg1.domain.riot.dto.champion.ChampionMasteryRankingDto;
import song.mygg1.domain.riot.entity.champion.ChampionMastery;
import song.mygg1.domain.riot.entity.champion.ChampionMasteryId;

import java.util.List;

@Repository
public interface ChampionMasterJpaRepository extends JpaRepository<ChampionMastery, ChampionMasteryId> {
    @Query("select new song.mygg1.domain.riot.dto.champion.ChampionMasteryRankingDto(" +
            " cm.championMasteryId.championId, " +
            " cm.championLevel, " +
            " cm.championPoints, " +
            " cm.championMasteryId.puuid, " +
            " sum(case when date(from_unixtime(i.gameCreation / 1000)) between :startDate and :endDate then 1 else 0 end) " +
            ") " +
            " from ChampionMastery cm " +
            " left join Participant p " +
            "   on p.puuid = cm.championMasteryId.puuid " +
            " left join p.info i " +
            "where cm.championMasteryId.championId = :championId " +
            "  and cm.championMasteryId.puuid in :ids " +
            "group by cm.championMasteryId.championId, " +
            "         cm.championLevel, " +
            "         cm.championPoints, " +
            "         cm.championMasteryId.puuid " +
            "order by cm.championPoints desc " +
            "limit 30")
    List<ChampionMasteryRankingDto> getChampionMasteryRanking(@Param("championId") Long championId,
                                                              @Param("ids") List<String> ids,
                                                              @Param("startDate") Long startDate,
                                                              @Param("endDate") Long endDate);
}
