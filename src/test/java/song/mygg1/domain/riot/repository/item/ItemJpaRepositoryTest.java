package song.mygg1.domain.riot.repository.item;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import song.mygg1.domain.riot.entity.item.Item;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@TestPropertySource(properties = {
        "spring.profiles.active=test",
        "JASYPT_ENCRYPTOR_PASSWORD=test"
})
class ItemJpaRepositoryTest {
    @Autowired
    ItemJpaRepository itemRepository;

    @Test
    void getItemByMap() {
        List<Item> itemList = itemRepository.findItemsByMap("11");
        log.info("itemList: {}", itemList);
    }

    @Test
    void getCoreItems() {
        List<Item> coreItemList = itemRepository.findCoreItemList();
        log.info("coreItemList: {}", coreItemList);
    }

}