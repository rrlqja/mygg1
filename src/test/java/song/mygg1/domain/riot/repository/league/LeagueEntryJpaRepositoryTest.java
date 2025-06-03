package song.mygg1.domain.riot.repository.league;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;
import song.mygg1.domain.riot.entity.league.LeagueEntry;

@Slf4j
@SpringBootTest
@TestPropertySource(properties = {
        "spring.profiles.active=test",
        "JASYPT_ENCRYPTOR_PASSWORD=test"
})
class LeagueEntryJpaRepositoryTest {
    @Autowired
    LeagueEntryJpaRepository leagueEntryRepository;

    @Test
    void getLeagueEntry() {
        Page<LeagueEntry> leagueEntryPage = leagueEntryRepository.findLeagueEntriesInRanker(PageRequest.of(0, 10));

        log.info("LeagueEntryPage: {}", leagueEntryPage.getContent());
    }
}