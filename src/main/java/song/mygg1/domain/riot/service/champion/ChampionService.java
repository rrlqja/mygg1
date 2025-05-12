package song.mygg1.domain.riot.service.champion;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import song.mygg1.domain.common.exception.riot.champion.exceptons.ChampionNotFoundException;
import song.mygg1.domain.common.exception.riot.riotapi.RiotApiException;
import song.mygg1.domain.common.service.DataDragonService;
import song.mygg1.domain.redis.service.CacheService;
import song.mygg1.domain.riot.dto.champion.ChampionDto;
import song.mygg1.domain.riot.entity.champion.Champion;
import song.mygg1.domain.riot.repository.champion.ChampionJpaRepository;
import song.mygg1.domain.riot.service.ApiService;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChampionService {
    private final CacheService<ChampionDto> cacheService;
    private final ChampionJpaRepository championRepository;
    private final DataDragonService dataDragonService;
    private final ApiService apiService;

    private static final Duration CHAMPION_TTL = Duration.ofDays(1);

    @Transactional
    @PostConstruct
    public void setChampionList() {
        JsonNode root = apiService.getChampionJson(dataDragonService.getVersion())
                .orElseThrow(() -> new RiotApiException("Failed to fetch champions"));

        JsonNode dataNode = root.path("data");
        List<Champion> championList = new ArrayList<>();

        Iterator<Map.Entry<String, JsonNode>> fields = dataNode.fields();
        while (fields.hasNext()) {
            JsonNode champNode = fields.next().getValue();
            Champion champion = Champion.create(
                    champNode.get("key").asLong(),
                    champNode.get("id").asText(),
                    champNode.get("name").asText(),
                    champNode.get("title").asText(),
                    champNode.get("blurb").asText()
            );
            championList.add(champion);
        }

        List<Champion> saved = championRepository.saveAll(championList);
        saved.forEach(champ -> {
            ChampionDto dto = new ChampionDto(champ);
            String key = "champion:key:" + champ.getKey();
            cacheService.put(key, dto, CHAMPION_TTL);
        });
    }

    @Transactional
    public List<ChampionDto> getChampion(List<Long> championIdList) {
        return championIdList.stream()
                .map(id -> {
                    String key = "champion:key:" + id;
                    return cacheService.getOrLoad(
                            key,
                            () -> championRepository.findChampionByKey(id)
                                    .map(ChampionDto::new)
                                    .orElseThrow(ChampionNotFoundException::new),
                            CHAMPION_TTL
                    );
                })
                .toList();
    }

    @Transactional
    public String getChampionId(Long championKey) {
        String key = "champion:key:" + championKey;

        return cacheService.getOrLoad(
                key,
                () -> new ChampionDto(
                        championRepository.findChampionByKey(championKey)
                                .orElseThrow(ChampionNotFoundException::new)),
                CHAMPION_TTL
        ).getId();
    }
}
