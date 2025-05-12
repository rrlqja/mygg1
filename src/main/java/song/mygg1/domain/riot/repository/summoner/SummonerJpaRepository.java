package song.mygg1.domain.riot.repository.summoner;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import song.mygg1.domain.riot.entity.summoner.Summoner;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface SummonerJpaRepository extends JpaRepository<Summoner, String> {
    @Query("select s " +
            " from Summoner s " +
            "where s.puuid = :puuid")
    Optional<Summoner> findSummonerByPuuid(@Param("puuid") String puuid);

    @Query("select s " +
            " from Summoner s " +
            "where s.id in :ids")
    List<Summoner> findSummonersByIdIn(@Param("ids") Collection<String> ids);

    @Query("select s " +
            " from Summoner s " +
            "where s.id = :id")
    Optional<Summoner> findSummonerById(@Param("id") String id);
}
