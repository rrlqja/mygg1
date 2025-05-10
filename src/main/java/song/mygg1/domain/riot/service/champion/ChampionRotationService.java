package song.mygg1.domain.riot.service.champion;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import song.mygg1.domain.redis.service.CacheService;
import song.mygg1.domain.riot.dto.champion.ChampionRotationsDto;
import song.mygg1.domain.riot.service.ApiService;

import java.time.Duration;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChampionRotationService {
    private final CacheService<ChampionRotationsDto> cacheService;
    private final ApiService apiService;

    private static final Duration TTL = Duration.ofHours(24);

    public List<Long> getFreeChampionIds() {
        return getRotations().getFreeChampionIds().stream()
                .map(Long::valueOf)
                .toList();
    }

    public List<Long> getFreeChampionIdsForNewPlayers() {
        return getRotations().getFreeChampionIdsForNewPlayers().stream()
                .map(Long::valueOf)
                .toList();
    }

    private ChampionRotationsDto getRotations() {
        String key = "champion:rotation:all";

        return cacheService.getOrLoad(
                key,
                apiService::getChampionRotations,
                TTL
        );
    }
}
