package song.mygg1.domain.riot.service.league;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;
import song.mygg1.domain.riot.dto.league.LeagueItemSummonerDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@TestPropertySource(properties = {
        "spring.profiles.active=test",
        "JASYPT_ENCRYPTOR_PASSWORD=test"
})
class LeagueItemServiceTest {
    @Autowired
    LeagueItemService leagueItemService;

    @Test
    void getRanker() {
        Page<LeagueItemSummonerDto> res = leagueItemService.getRankLeagueItemList(LeagueListService.CHALLENGER_LEAGUE_ID, PageRequest.of(0, 10));

        log.info("res: {}", res);
    }

}