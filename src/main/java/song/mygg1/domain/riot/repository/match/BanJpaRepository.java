package song.mygg1.domain.riot.repository.match;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import song.mygg1.domain.riot.entity.match.team.Ban;

@Repository
public interface BanJpaRepository extends JpaRepository<Ban, Long> {
    @Query("select count(distinct b.team.id.infoId) " +
            " from Ban b " +
            "where b.championId = :championId " +
            "  and date(from_unixtime(b.team.info.gameCreation / 1000)) between :startDate and :endDate ")
    Long countBanChampionInGameCreation(@Param("championId") Integer championId,
                                        @Param("startDate") Long startDate,
                                        @Param("endDate") Long endDate);
}
