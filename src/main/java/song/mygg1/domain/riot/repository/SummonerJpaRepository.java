package song.mygg1.domain.riot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import song.mygg1.domain.riot.entity.summoner.Summoner;

import java.util.Optional;

@Repository
public interface SummonerJpaRepository extends JpaRepository<Summoner, String> {
    @Query("select s " +
            " from Summoner s " +
            "where s.puuid = :puuid")
    Optional<Summoner> findSummonerByPuuid(@Param("puuid") String puuid);
}
