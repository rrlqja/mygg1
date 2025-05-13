package song.mygg1.domain.riot.repository.match;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import song.mygg1.domain.riot.dto.match.participant.ChampionWinRatePerDateDto;

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
class ParticipantJpaRepositoryTest {
    @Autowired
    ParticipantJpaRepository participantRepository;
    @Autowired
    ObjectMapper mapper;

    @Test
    void getChampionWinRatePerDate() {
        LocalDate yesterday = LocalDate.now(ZoneId.of("Asia/Seoul")).minusDays(1);
        LocalDate startDate = yesterday.minusDays(6);

        long startMs = startDate
                .atStartOfDay(ZoneId.of("Asia/Seoul"))
                .toInstant()
                .toEpochMilli();
        long endMs = yesterday
                .atTime(LocalTime.MAX)
                .atZone(ZoneId.of("Asia/Seoul"))
                .toInstant()
                .toEpochMilli();
        List<ChampionWinRatePerDateDto> varusList = participantRepository.findChampionDailyWinRate("Varus", startMs, endMs);

        log.info("varusList: {}", varusList);
    }

}