package song.mygg.domain.loa.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
class AdventurerJpaRepositoryTest {
    @Autowired
    AdventurerJpaRepository adventurerJpaRepository;

    @ParameterizedTest
    @ValueSource(strings = {"test@1@2Name", "test@!홍길동"})
    void successFindEmptyByName(String name) {
        assertThat(adventurerJpaRepository.findByName(name).isEmpty())
                .isTrue();
    }

}