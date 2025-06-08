package song.mygg1.domain.riot.service.rune;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import song.mygg1.domain.riot.repository.rune.RuneStyleJpaRepository;

import java.io.IOException;

@Slf4j
@SpringBootTest
@TestPropertySource(properties = {
        "spring.profiles.active=test",
        "JASYPT_ENCRYPTOR_PASSWORD=test"
})
class RuneServiceTest {
    @Autowired
    RuneService runeService;

    @Autowired
    RuneStyleJpaRepository runeStyleRepository;

    @Test
    void getRuneStyles() throws IOException {
        runeService.setRuneStyles();
    }

}