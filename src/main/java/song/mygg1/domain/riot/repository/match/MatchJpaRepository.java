package song.mygg1.domain.riot.repository.match;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import song.mygg1.domain.riot.dto.match.championbuild.MatchPlayerInfo;
import song.mygg1.domain.riot.entity.match.Matches;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface MatchJpaRepository extends JpaRepository<Matches, Long> {
    @Query("select m " +
            " from Matches m " +
            " join fetch m.info " +
            " join fetch m.metadata " +
            "where m.matchId in :matchIds")
    List<Matches> findMatchesByMatchIdIn(@Param("matchIds") Collection<String> matchIds);

    @Query("select m " +
            " from Matches m " +
            " join fetch m.info " +
            " join fetch m.metadata " +
            "where m.matchId = :matchId")
    Optional<Matches> findMatchesByMatchId(@Param("matchId") String matchId);

    @Query("select distinct m " +
            " from Matches m " +
            " join fetch m.metadata mm" +
            " join fetch m.info mi" +
            " join m.info.participants p " +
            "where p.championId = :championId " +
            "order by mi.gameCreation desc")
    Page<Matches> findMatchesByChampion(@Param("championId") Long championId,
                                        Pageable pageable);

    @Query("select new song.mygg1.domain.riot.dto.match.championbuild.MatchPlayerInfo(m.matchId, p.id.participantId, p.win) " +
            " from Matches m " +
            " join m.info i " +
            " join i.participants p " +
            "where date(from_unixtime(i.gameCreation / 1000)) BETWEEN :startTime AND :endTime " +
            "  and p.championId = :championId " +
            "order by i.gameCreation asc")
    List<MatchPlayerInfo> findMatchPlayerInfoByChampionAndPeriod(
            @Param("championId") Integer championId,
            @Param("startTime") Long startTime,
            @Param("endTime") Long endTime,
            Pageable pageable
    );
}
