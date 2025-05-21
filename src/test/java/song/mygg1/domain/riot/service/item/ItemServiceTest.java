package song.mygg1.domain.riot.service.item;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import song.mygg1.domain.redis.service.CacheService;
import song.mygg1.domain.riot.dto.item.ItemDto;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@TestPropertySource(properties = {
        "spring.profiles.active=test",
        "JASYPT_ENCRYPTOR_PASSWORD=test"
})
class ItemServiceTest {
    @Autowired
    ItemService itemService;
    @Autowired
    CacheService<ItemDto> cacheService;

    @Test
    void getItem() {
//        Optional<ItemDto> itemDto = cacheService.get("item:id:1001");
//        Assertions.assertThat(itemDto).isPresent();
//        log.info(itemDto.get().toString());
    }
}