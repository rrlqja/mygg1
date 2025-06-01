package song.mygg1.domain.riot.repository.match;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@TestPropertySource(properties = {
        "spring.profiles.active=test",
        "JASYPT_ENCRYPTOR_PASSWORD=test"
})
class InfoJpaRepositoryTest {
    @Autowired
    InfoJpaRepository infoRepository;

    private static final ZoneId kst = ZoneId.of("Asia/Seoul");

    @Test
    public void getTotalInfo() {
        LocalDate yesterday = LocalDate.now(kst).minusDays(1);
        LocalDate startDate = yesterday.minusDays(6);

        Instant startInstant = startDate
                .atStartOfDay(kst)
                .toInstant();
        Instant endInstant = yesterday
                .atTime(LocalTime.MAX)
                .atZone(kst)
                .toInstant();

        long start = startInstant.toEpochMilli();
        long end = endInstant.toEpochMilli();

        Long count = infoRepository.countTotalGamesInGameCreation(start, end);
        log.info("Total games in game creation: {}", count);
    }

}