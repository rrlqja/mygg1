package song.mygg1.domain.riot.repository.league;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import song.mygg1.domain.riot.entity.league.LeagueList;

@Repository
public interface LeagueListJpaRepository extends JpaRepository<LeagueList, String> {

}
