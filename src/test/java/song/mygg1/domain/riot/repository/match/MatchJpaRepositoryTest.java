package song.mygg1.domain.riot.repository.match;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import song.mygg1.domain.riot.dto.match.championbuild.MatchPlayerInfo;
import song.mygg1.domain.riot.entity.match.Matches;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@TestPropertySource(properties = {
        "spring.profiles.active=test",
        "JASYPT_ENCRYPTOR_PASSWORD=test"
})
class MatchJpaRepositoryTest {
    @Autowired
    MatchJpaRepository matchRepository;
    @Autowired
    ObjectMapper mapper;

    private static final ZoneId kst = ZoneId.of("Asia/Seoul");

    @Test
    @Transactional
    void getChampionMatchList() {
        Assertions.assertDoesNotThrow(() -> matchRepository.findMatchesByChampion(110L, PageRequest.of(0, 10)).getContent());
    }

    @Test
    void getMatchPlayerInfo() {
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
        List<MatchPlayerInfo> infoList = matchRepository.findMatchPlayerInfoByChampionAndPeriod(81, start, end);
        log.info("infoList: {}", infoList.size());
    }
}