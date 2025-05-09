package song.mygg1.domain.riot.service.champion;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import song.mygg1.domain.common.exception.riot.champion.ChampionNotFoundException;
import song.mygg1.domain.common.exception.riot.riotapi.RiotApiException;
import song.mygg1.domain.common.service.DataDragonService;
import song.mygg1.domain.riot.dto.champion.ChampionDto;
import song.mygg1.domain.riot.entity.champion.Champion;
import song.mygg1.domain.riot.repository.champion.ChampionJpaRepository;
import song.mygg1.domain.riot.service.ApiService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChampionService {
    private final ChampionJpaRepository championRepository;
    private final DataDragonService dataDragonService;
    private final ApiService apiService;

    @Transactional
    public void setChampionList() {
        JsonNode root = apiService.getChampionJson(dataDragonService.getVersion())
                .orElseThrow(RiotApiException::new);

        JsonNode dataNode = root.path("data");
        List<Champion> championList = new ArrayList<>();

        Iterator<Map.Entry<String, JsonNode>> fields = dataNode.fields();
        while (fields.hasNext()) {
            JsonNode champNode = fields.next().getValue();
            Champion champion = Champion.create(champNode.get("key").asLong(),
                    champNode.get("id").asText(),
                    champNode.get("name").asText(),
                    champNode.get("title").asText(),
                    champNode.get("blurb").asText());

            championList.add(champion);
        }

        championRepository.saveAll(championList);
    }

    @Transactional
    public String getChampionId(Long championKey) {
        Champion champion = championRepository.findChampionByKey(championKey)
                .orElseThrow(ChampionNotFoundException::new);

        return champion.getId();
    }

    public List<ChampionDto> getChampion(List<Long> championIdList) {
        List<Champion> championList = championRepository.findChampionByKeyIn(championIdList);

        return championList.stream().map(ChampionDto::new).toList();
    }
}
