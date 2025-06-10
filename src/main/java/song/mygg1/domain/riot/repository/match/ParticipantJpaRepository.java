package song.mygg1.domain.riot.repository.match;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import song.mygg1.domain.riot.dto.champion.RuneCountDto;
import song.mygg1.domain.riot.dto.match.participant.ChampionWinRatePerDateDto;
import song.mygg1.domain.riot.dto.match.participant.WinRateDto;
import song.mygg1.domain.riot.entity.match.Participant;
import song.mygg1.domain.riot.entity.match.ParticipantId;
import song.mygg1.domain.riot.entity.match.participant.PerkStyle;

import java.util.List;

@Repository
public interface ParticipantJpaRepository extends JpaRepository<Participant, ParticipantId> {
    @Query("select new song.mygg1.domain.riot.dto.match.participant.ChampionWinRatePerDateDto( " +
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

    @Query("select new song.mygg1.domain.riot.dto.match.participant.WinRateDto( " +
            " p.championName, " +
            " p.championId, " +
            " sum(case when date(from_unixtime(i.gameCreation / 1000)) between :curStartDate and :curEndDate then 1 else 0 end), " +
            " sum(case when date(from_unixtime(i.gameCreation / 1000)) between :curStartDate and :curEndDate and p.win = true then 1 else 0 end), " +
            " round((sum(case when date(from_unixtime(i.gameCreation / 1000)) between :curStartDate and :curEndDate and p.win = true then 1 else 0 end) * 100.0) " +
            "   / nullif(sum(case when date(from_unixtime(i.gameCreation / 1000)) between :curStartDate and :curEndDate then 1 else 0 end), 0), 2), " +
            " sum(case when date(from_unixtime(i.gameCreation / 1000)) between :prevStartDate and :prevEndDate then 1 else 0 end), " +
            " sum(case when date(from_unixtime(i.gameCreation / 1000)) between :prevStartDate and :prevEndDate and p.win = true then 1 else 0 end), " +
            " round((sum(case when date(from_unixtime(i.gameCreation / 1000)) between :prevStartDate and :prevEndDate and p.win = true then 1 else 0 end) * 100.0) " +
            "   / nullif(sum(case when date(from_unixtime(i.gameCreation / 1000)) between :prevStartDate and :prevEndDate then 1 else 0 end), 0), 2) " +
            ") " +
            "  from Participant p " +
            "  join p.info i" +
            " group by p.championName, p.championId " +
            "having sum(case when date(from_unixtime(i.gameCreation / 1000)) between :curStartDate and :curEndDate then 1 else 0 end) > 0 " +
            "    or sum(case when date(from_unixtime(i.gameCreation / 1000)) between :prevStartDate and :prevEndDate then 1 else 0 end) > 0 " +
            " order by 3 desc")
    List<WinRateDto> findWinRateList(@Param("curStartDate") Long curStartDate,
                                     @Param("curEndDate") Long curEndDate,
                                     @Param("prevStartDate") Long prevStartDate,
                                     @Param("prevEndDate") Long prevEndDate,
                                     Pageable pageable);

    @Query("select count(distinct p.info.gameId)" +
            " from Participant p " +
            "where p.championId = :championId " +
            "  and date(from_unixtime(p.info.gameCreation / 1000)) between :startDate and :endDate ")
    Long countPickedChampionInGameCreation(@Param("championId") Integer championId,
                                           @Param("startDate") Long startDate,
                                           @Param("endDate") Long endDate);

    @Query("select count(p.info.gameId) " +
            " from Participant p " +
            "where p.championId = :championId " +
            "  and p.win = true " +
            "  and date(from_unixtime(p.info.gameCreation / 1000)) between :startDate and :endDate ")
    Long countWinChampionInGameCreation(@Param("championId") Integer championId,
                                        @Param("startDate") Long startDate,
                                        @Param("endDate") Long endDate);

    @Query("SELECT COUNT(p) FROM Participant p " +
            "JOIN p.perks perks " +
            "WHERE p.championId = :championId " +
            "AND date(from_unixtime(p.info.gameCreation / 1000)) between :startDate and :endDate")
    long countTotalGames(@Param("championId") Integer championId,
                         @Param("startDate") long startDate,
                         @Param("endDate") long endDate);

    @Query("SELECT NEW song.mygg1.domain.riot.dto.champion.RuneCountDto(ps.style, COUNT(ps.style)) " +
            "FROM Participant p JOIN p.perks.styles ps " +
            "WHERE p.championId = :championId " +
            "AND date(from_unixtime(p.info.gameCreation / 1000)) between :startDate and :endDate " +
            "AND ps.description = 'primaryStyle' " +
            "GROUP BY ps.style")
    List<RuneCountDto> findPrimaryStylePicks(@Param("championId") Integer championId,
                                             @Param("startDate") long startDate,
                                             @Param("endDate") long endDate);

    @Query("SELECT NEW song.mygg1.domain.riot.dto.champion.RuneCountDto(ps.style, COUNT(ps.style)) " +
            "FROM Participant p JOIN p.perks.styles ps " +
            "WHERE p.championId = :championId " +
            "AND date(from_unixtime(p.info.gameCreation / 1000)) between :startDate and :endDate " +
            "AND ps.description = 'subStyle' " +
            "GROUP BY ps.style")
    List<RuneCountDto> findSecondaryStylePicks(@Param("championId") Integer championId,
                                               @Param("startDate") long startDate,
                                               @Param("endDate") long endDate);

    @Query("SELECT DISTINCT ps FROM PerkStyle ps " +
            "JOIN FETCH ps.selections " +
            "JOIN ps.perks pr " +
            "JOIN pr.participant p " +
            "WHERE p.championId = :championId " +
            "AND date(from_unixtime(p.info.gameCreation / 1000)) between :startDate and :endDate")
    List<PerkStyle> findPerkStylesWithSelections(@Param("championId") Integer championId,
                                                 @Param("startDate") long startDate,
                                                 @Param("endDate") long endDate);

    @Query("SELECT NEW song.mygg1.domain.riot.dto.champion.RuneCountDto(p.perks.statPerks.offense, COUNT(p.perks.statPerks.offense)) " +
            "FROM Participant p JOIN p.perks perks " +
            "WHERE p.championId = :championId " +
            "AND date(from_unixtime(p.info.gameCreation / 1000)) between :startDate and :endDate " +
            "GROUP BY p.perks.statPerks.offense")
    List<RuneCountDto> findStatPerkOffensePicks(@Param("championId") Integer championId,
                                                @Param("startDate") long startDate,
                                                @Param("endDate") long endDate);

    @Query("SELECT NEW song.mygg1.domain.riot.dto.champion.RuneCountDto(p.perks.statPerks.flex, COUNT(p.perks.statPerks.flex)) " +
            "FROM Participant p JOIN p.perks perks " +
            "WHERE p.championId = :championId " +
            "AND date(from_unixtime(p.info.gameCreation / 1000)) between :startDate and :endDate " +
            "GROUP BY p.perks.statPerks.flex")
    List<RuneCountDto> findStatPerkFlexPicks(@Param("championId") Integer championId,
                                             @Param("startDate") long startDate,
                                             @Param("endDate") long endDate);

    @Query("SELECT NEW song.mygg1.domain.riot.dto.champion.RuneCountDto(p.perks.statPerks.defense, COUNT(p.perks.statPerks.defense)) " +
            "FROM Participant p JOIN p.perks perks " +
            "WHERE p.championId = :championId " +
            "AND date(from_unixtime(p.info.gameCreation / 1000)) between :startDate and :endDate " +
            "GROUP BY p.perks.statPerks.defense")
    List<RuneCountDto> findStatPerkDefensePicks(@Param("championId") Integer championId,
                                                @Param("startDate") long startDate,
                                                @Param("endDate") long endDate);
}
