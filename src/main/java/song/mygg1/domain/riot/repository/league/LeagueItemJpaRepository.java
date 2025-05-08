package song.mygg1.domain.riot.repository.league;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import song.mygg1.domain.riot.entity.league.LeagueItem;

import java.util.Collection;
import java.util.List;

@Repository
public interface LeagueItemJpaRepository extends JpaRepository<LeagueItem, String> {
    @Query("select li " +
            " from LeagueItem li " +
            "where li.summonerId in :summonerIds")
    List<LeagueItem> findLeagueItemsBySummonerIdIn(@Param("summonerIds") Collection<String> summonerIds);
}
