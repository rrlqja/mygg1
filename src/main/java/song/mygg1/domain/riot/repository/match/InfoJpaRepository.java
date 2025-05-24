package song.mygg1.domain.riot.repository.match;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import song.mygg1.domain.riot.dto.match.info.ChampionUsageDto;
import song.mygg1.domain.riot.entity.match.Info;

import java.util.List;

@Repository
public interface InfoJpaRepository extends JpaRepository<Info, Long> {
    @Query("select new song.mygg1.domain.riot.dto.match.info.ChampionUsageDto(" +
            " date(from_unixtime(i.gameCreation / 1000)), " +
            "count(distinct i.gameId), " +
            "  sum(case when p.championId = :championId then 1 else 0 end), " +
            " (sum(case when p.championId = :championId then 1 else 0 end) * 100.0 ) / nullif(count(distinct i.gameId), 0) " +
            ") " +
            "  from Info i " +
            "  left join i.participants p " +
            " where date(from_unixtime(i.gameCreation / 1000)) between :startDate and :endDate " +
            " group by date(from_unixtime(i.gameCreation / 1000)) " +
            " order by date(from_unixtime(i.gameCreation / 1000)) asc")
    List<ChampionUsageDto> findChampionUsage(@Param("championId") Long championId,
                                             @Param("startDate") Long startDate,
                                             @Param("endDate") Long endDate);
}
