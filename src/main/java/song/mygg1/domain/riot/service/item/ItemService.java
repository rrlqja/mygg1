package song.mygg1.domain.riot.service.item;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import song.mygg1.domain.common.exception.riot.riotapi.RiotApiException;
import song.mygg1.domain.riot.service.datadragon.DataDragonService;
import song.mygg1.domain.redis.service.CacheService;
import song.mygg1.domain.riot.dto.item.ItemDto;
import song.mygg1.domain.riot.dto.item.ItemResponseDto;
import song.mygg1.domain.riot.entity.item.Item;
import song.mygg1.domain.riot.mapper.item.ItemMapper;
import song.mygg1.domain.riot.repository.item.ItemJpaRepository;
import song.mygg1.domain.riot.service.ApiService;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {
    private final CacheService<ItemDto> cacheService;
    private final DataDragonService dataDragonService;
    private final ItemJpaRepository itemRepository;
    private final ApiService apiService;
    private final ObjectMapper objectMapper;
    private final ItemMapper itemMapper;

    private static final Duration ITEM_TTL = Duration.ofDays(1);

    @PostConstruct
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void setItemList() throws JsonProcessingException {
        JsonNode root = apiService.getItemJson(dataDragonService.getVersion())
                .orElseThrow(() -> new RiotApiException("Failed to fetch items"));

        List<Item> itemList = new ArrayList<>();

        ItemResponseDto itemResponse = objectMapper.treeToValue(root, ItemResponseDto.class);
        itemResponse.getData().forEach((key, value) -> {
            log.info("data key: {}, value: {}", key, value);
            Item entity = itemMapper.toEntity(value);
            itemList.add(entity);
        });

        List<Item> saved = itemRepository.saveAll(itemList);

        saved.forEach(item -> {
            ItemDto dto = itemMapper.toDto(item);
            String id = "item:id:" + item.getId();
            cacheService.put(id, dto, ITEM_TTL);
        });
    }
}
