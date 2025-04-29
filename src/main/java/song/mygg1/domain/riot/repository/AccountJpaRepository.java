package song.mygg1.domain.riot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import song.mygg1.domain.riot.entity.account.Account;

import java.util.Optional;

@Repository
public interface AccountJpaRepository extends JpaRepository<Account, Long> {
    @Query("select a " +
            " from Account a " +
            "where a.gameName = :gameName " +
            "  and a.tagLine = :tagLine")
    Optional<Account> findAccountByGameNameAndTagLine(@Param("gameName") String gameName,
                                                      @Param("tagLine") String tagLine);
}
