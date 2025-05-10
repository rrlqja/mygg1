package song.mygg1.domain.riot.repository.match;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import song.mygg1.domain.riot.dto.match.ChampionWinRatePerDateDto;
import song.mygg1.domain.riot.entity.match.Participant;
import song.mygg1.domain.riot.entity.match.ParticipantId;

import java.util.List;

@Repository
public interface ParticipantJpaRepository extends JpaRepository<Participant, ParticipantId> {
    @Query("select new song.mygg1.domain.riot.dto.match.ChampionWinRatePerDateDto( " +
            " date(from_unixtime(i.gameCreation / 1000)), " +
            " p.championName, " +
            " count(p), " +
            " sum(case when p.win = true then 1 else 0 end), " +
            " sum(case when p.win = false then 1 else 0 end), " +
            " round(sum(case when p.win = true then 1 else 0 end) * 100.0 / count(*),2)" +
            ") " +
            " from Participant p " +
            " join p.info i " +
            "where p.championName = :championName " +
            "  and i.gameCreation between :start and :end " +
            "group by p.championName, date(from_unixtime(i.gameCreation / 1000)) " +
            "order by date(from_unixtime(i.gameCreation / 1000)) asc ")
    List<ChampionWinRatePerDateDto> findChampionDailyWinRate(@Param("championName") String championName,
                                                             @Param("start") Long start,
                                                             @Param("end") Long end);
}
