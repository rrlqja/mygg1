package song.mygg1.domain.riot.service.champion;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import song.mygg1.domain.redis.service.CacheService;
import song.mygg1.domain.riot.dto.champion.ChampionMasteryDto;
import song.mygg1.domain.riot.entity.champion.ChampionMastery;
import song.mygg1.domain.riot.mapper.champion.ChampionMasterMapper;
import song.mygg1.domain.riot.repository.mastery.ChampionMasteryJpaRepository;
import song.mygg1.domain.riot.service.ApiService;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChampionMasteryService {
    private final CacheService<ChampionMasteryDto> cacheService;
    private final ChampionMasteryJpaRepository championMasteryRepository;
    private final ApiService apiService;
    private final ChampionMasterMapper mapper;
    private final StringRedisTemplate redisTemplate;

    private static final Duration MASTERY_TTL = Duration.ofHours(1);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<ChampionMasteryDto> loadChampionMastery(String puuid, Pageable pageable) {
        Page<ChampionMastery> page = championMasteryRepository
                .findChampionMasteriesByPuuid(puuid, pageable);

        if (page.hasContent()) {
            return page.map(mapper::toDto).toList();
        }

        List<ChampionMasteryDto> apiDtos = apiService.getChampionMastery(puuid);
        List<ChampionMastery> newEntities = apiDtos.stream()
                .map(mapper::toEntity)
                .toList();
        championMasteryRepository.saveAll(newEntities);

        newEntities.forEach(ent -> {
            ChampionMasteryDto dto = mapper.toDto(ent);
            cacheService.put(cacheKey(puuid, dto.getChampionId()), dto, MASTERY_TTL);
        });

        int start  = (int) pageable.getOffset();
        int end    = Math.min(start + pageable.getPageSize(), apiDtos.size());
        List<ChampionMasteryDto> content = apiDtos.subList(start, end);

        return new PageImpl<>(content, pageable, apiDtos.size()).toList();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<ChampionMasteryDto> refreshChampionMaster(String puuid) {
        List<ChampionMasteryDto> dtos = apiService.getChampionMastery(puuid);

        List<ChampionMastery> entityList = dtos.stream()
                .map(mapper::toEntity)
                .toList();
        List<ChampionMastery> saved = championMasteryRepository.saveAll(entityList);

        saved.forEach(entity -> cacheService.put(
                cacheKey(puuid, entity.getChampionMasteryId().getChampionId()),
                mapper.toDto(entity),
                MASTERY_TTL));

        return saved.stream()
                .map(mapper::toDto)
                .sorted(Comparator.comparingInt(ChampionMasteryDto::getChampionPoints).reversed())
                .limit(10)
                .toList();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<ChampionMasteryDto> getChampionMastery(String puuid, int count) {
        String keyPattern = "champion:mastery:" + puuid + ":*";
        Set<String> keys = redisTemplate.keys(keyPattern);

        List<ChampionMasteryDto> result = new ArrayList<>();

        if (keys != null && !keys.isEmpty()) {
            for (String key : keys) {
                cacheService.get(key).ifPresent(result::add);
            }
        }

        if (result.size() < count) {
            int needed = count - result.size();
            List<ChampionMastery> dbEnts = championMasteryRepository
                    .findChampionMasteriesByPuuid(puuid, PageRequest.of(0, count)).toList();
            for (ChampionMastery ent : dbEnts) {
                ChampionMasteryDto dto = mapper.toDto(ent);
                boolean already = result.stream()
                        .anyMatch(d -> d.getChampionId().equals(dto.getChampionId()));
                if (!already) {
                    String key = cacheKey(puuid, dto.getChampionId());
                    cacheService.put(key, dto, MASTERY_TTL);
                    result.add(dto);
                    if (--needed == 0) break;
                }
            }
        }

        if (result.size() < count) {
            int needed = count - result.size();
            List<ChampionMasteryDto> apiDtos = apiService.getChampionMastery(puuid);
            for (ChampionMasteryDto dto : apiDtos) {
                boolean already = result.stream()
                        .anyMatch(d -> d.getChampionId().equals(dto.getChampionId()));
                if (!already) {
                    ChampionMastery ent = mapper.toEntity(dto);
                    championMasteryRepository.save(ent);
                    String key = cacheKey(puuid, dto.getChampionId());
                    cacheService.put(key, dto, MASTERY_TTL);
                    result.add(dto);
                    if (--needed == 0) break;
                }
            }
        }

        return result.stream()
                .sorted(Comparator.comparingInt(ChampionMasteryDto::getChampionPoints).reversed())
                .limit(count)
                .toList();
    }

    private String cacheKey(String puuid, Long championId) {
        return "champion:mastery:" + puuid + ":" + championId;
    }
}
