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
import song.mygg1.domain.riot.entity.match.Matches;

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

    @Test
    @Transactional
    void getChampionMatchList() {
        Assertions.assertDoesNotThrow(() -> matchRepository.findMatchesByChampion(110L, PageRequest.of(0, 10)).getContent());
    }
}