package song.mygg1.domain.riot.repository.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import song.mygg1.domain.riot.entity.account.Account;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountJpaRepository extends JpaRepository<Account, Long> {
    @Query("select a " +
            " from Account a " +
            "where a.gameName = :gameName " +
            "  and a.tagLine = :tagLine")
    Optional<Account> findAccountByGameNameAndTagLine(@Param("gameName") String gameName,
                                                      @Param("tagLine") String tagLine);

    @Query("select a " +
            " from Account a " +
            "where a.puuid = :puuid")
    Optional<Account> findAccountByPuuid(@Param("puuid") String puuid);

    @Query("select a " +
            " from Account a " +
            "where a.puuid in :puuids")
    List<Account> findAccountsByPuuidIn(@Param("puuids") Collection<String> puuids);
}
