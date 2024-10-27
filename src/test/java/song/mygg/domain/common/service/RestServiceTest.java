package song.mygg.domain.common.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import song.mygg.domain.common.entity.CommonEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
class RestServiceTest {
    @Autowired
    RestService restService;

    @Test
    void successFindJSPH() {
        Optional<CommonEntity> response = restService.findEntity("https://jsonplaceholder.typicode.com/todos/1", CommonEntity.class);

        assertThat(response.isPresent())
                .isTrue();
    }

    @Test
    void failFindJSPH() {
        Optional<CommonEntity> response = restService.findEntity("testUrl", CommonEntity.class);

        assertThat(response.isEmpty())
                .isTrue();
    }

}