package song.mygg1.domain.riot.repository.timeline;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import song.mygg1.domain.riot.entity.timeline.events.ItemPurchaseEvent;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@TestPropertySource(properties = {
        "spring.profiles.active=test",
        "JASYPT_ENCRYPTOR_PASSWORD=test"
})
class ItemPurchaseEventJpaRepositoryTest {
    @Autowired
    ItemPurchaseEventJpaRepository itemPurchaseEventRepository;

    @Test
    void getItemPurchaseEvent() {
        List<ItemPurchaseEvent> itemPurchaseEventList = itemPurchaseEventRepository.findByMatchIdAndParticipantIdOrderByTimestampAsc("KR_7645712278", 2);
        log.info("itemPurchaseEventList: {}", itemPurchaseEventList.size());
    }

}