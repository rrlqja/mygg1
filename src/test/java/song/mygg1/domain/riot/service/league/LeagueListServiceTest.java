package song.mygg1.domain.riot.service.league;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import song.mygg1.domain.riot.dto.league.LeagueListDto;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@TestPropertySource(properties = {
        "spring.profiles.active=test",
        "JASYPT_ENCRYPTOR_PASSWORD=test"
})
class LeagueListServiceTest {
    @Autowired
    LeagueListService leagueListService;

    @Test
    void getChallenger() {
//        LeagueListDto res = leagueListService.getChallengerLeagueList();
        LeagueListDto res = leagueListService.refreshChallengerLeague();

        log.info("leagueList: {}", res);
    }

}