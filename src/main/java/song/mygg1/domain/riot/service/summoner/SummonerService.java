package song.mygg1.domain.riot.service.summoner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import song.mygg1.domain.common.exception.riot.summoner.exceptions.SummonerNotFoundException;
import song.mygg1.domain.redis.service.CacheService;
import song.mygg1.domain.riot.dto.summoner.SummonerDto;
import song.mygg1.domain.riot.entity.summoner.Summoner;
import song.mygg1.domain.riot.mapper.summoner.SummonerMapper;
import song.mygg1.domain.riot.repository.summoner.SummonerJpaRepository;
import song.mygg1.domain.riot.service.ApiService;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SummonerService {
    private final CacheService<SummonerDto> cacheService;
    private final SummonerJpaRepository summonerRepository;
    private final ApiService apiService;
    private final SummonerMapper summonerMapper;

    private static final Duration SUMMONER_TTL = Duration.ofHours(3);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public SummonerDto getSummoner(String puuid) {
        String key = "summoner:puuid:" + puuid;

        return cacheService.getOrLoad(
                key,
                () -> summonerMapper.toDto(getOrFetchSummoner(puuid)),
                SUMMONER_TTL
        );
    }

    private Summoner getOrFetchSummoner(String puuid) {
        return summonerRepository.findSummonerByPuuid(puuid)
                .orElseGet(() -> {
                    SummonerDto summonerDto = apiService.getSummoner(puuid)
                            .orElseThrow(() -> {
                                log.warn("Riot Api: {} 소환사를 찾을 수 없습니다.", puuid);
                                return new SummonerNotFoundException("사용자를 찾을 수 없습니다.");
                            });
                    Summoner entity = summonerMapper.toEntity(summonerDto);

                    return summonerRepository.save(entity);
                });
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Summoner> getSummonerList(List<String> summonerIdList) {
        List<Summoner> existingSummoerList = summonerRepository.findSummonersByIdIn(summonerIdList);

        Set<String> found = existingSummoerList.stream()
                .map(Summoner::getId)
                .collect(Collectors.toSet());
        List<String> missingSummonerList = summonerIdList.stream()
                .filter(p -> !found.contains(p))
                .toList();

        List<SummonerDto> fetchDtoList = missingSummonerList.stream()
                .map(apiService::getSummonerBySummonerId)
                .flatMap(Optional::stream)
                .toList();

        List<Summoner> fetchedEntities = fetchDtoList.stream()
                .map(SummonerDto::toEntity)
                .toList();

        List<Summoner> saved = summonerRepository.saveAll(fetchedEntities);

        existingSummoerList.addAll(saved);
        return existingSummoerList;
    }

    @Transactional
    public SummonerDto refreshSummoner(String puuid) {
        SummonerDto puuidSummoner = apiService.getSummoner(puuid)
                .orElseThrow(SummonerNotFoundException::new);

        Summoner summoner = summonerRepository.findSummonerByPuuid(puuidSummoner.getPuuid())
                .orElseThrow(SummonerNotFoundException::new);

        summoner.update(puuidSummoner.getSummonerLevel(), puuidSummoner.getProfileIconId());
        Summoner updated = summonerRepository.save(summoner);

        String key = "summoner:puuid:" + puuid;
        SummonerDto dto = summonerMapper.toDto(updated);
        cacheService.put(key, dto, SUMMONER_TTL);

        return dto;
    }
}
