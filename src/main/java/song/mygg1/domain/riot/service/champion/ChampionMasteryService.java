package song.mygg1.domain.riot.service.champion;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import song.mygg1.domain.riot.dto.champion.ChampionMasteryDto;
import song.mygg1.domain.riot.entity.champion.ChampionMastery;
import song.mygg1.domain.riot.repository.mastery.ChampionMasteryJpaRepository;
import song.mygg1.domain.riot.service.ApiService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChampionMasteryService {
    private final ChampionMasteryJpaRepository championMasteryRepository;
    private final ApiService apiService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<ChampionMastery> getChampionMastery(String puuid, Integer count) {
        List<ChampionMasteryDto> championMasteryDtoList = apiService.getChampionMastery(puuid, count);
        List<ChampionMastery> cmList = championMasteryDtoList.stream()
                .map(ChampionMasteryDto::toEntity)
                .toList();

        championMasteryRepository.saveAll(cmList);

        return championMasteryRepository.findChampionMasteryByPuuidInTop3(puuid);
    }
}
