package song.mygg1.domain.riot.repository.match;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@TestPropertySource(properties = {
        "spring.profiles.active=test",
        "JASYPT_ENCRYPTOR_PASSWORD=test"
})
class BanJpaRepositoryTest {
    @Autowired
    BanJpaRepository banRepository;

    @Test
    void getBanCount() {
//        banRepository.countBanChampionInGameCreation()
    }

}