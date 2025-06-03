package song.mygg1.domain.riot.service.league;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import song.mygg1.domain.common.exception.riot.league.exceptions.LeagueListNotFoundException;
import song.mygg1.domain.redis.service.CacheService;
import song.mygg1.domain.riot.dto.league.LeagueItemDto;
import song.mygg1.domain.riot.dto.league.LeagueListDto;
import song.mygg1.domain.riot.entity.league.LeagueItem;
import song.mygg1.domain.riot.entity.league.LeagueList;
import song.mygg1.domain.riot.entity.league.LeagueQueue;
import song.mygg1.domain.riot.mapper.league.LeagueItemMapper;
import song.mygg1.domain.riot.mapper.league.LeagueListMapper;
import song.mygg1.domain.riot.repository.league.LeagueListJpaRepository;
import song.mygg1.domain.riot.service.ApiService;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LeagueListService {
    private final CacheService<LeagueListDto> leagueListCacheService;
    private final LeagueListJpaRepository leagueListRepository;
    private final ApiService apiService;
    private final LeagueListMapper leagueListMapper;
    private final LeagueItemMapper leagueItemMapper;

    public static final String CHALLENGER_LEAGUE_ID = "CHALLENGER_" + LeagueQueue.RANKED_SOLO_5x5.getQueue();
    public static final String GRANDMASTER_LEAGUE_ID = "GRANDMASTER_" + LeagueQueue.RANKED_SOLO_5x5.getQueue();
    public static final String MASTER_LEAGUE_ID = "MASTER_" + LeagueQueue.RANKED_SOLO_5x5.getQueue();

    private static final String CHALLENGER_CACHE_KEY = "league:list:challenger";
    private static final String GRANDMASTER_CACHE_KEY = "league:list:grandmaster";
    private static final String MASTER_CACHE_KEY = "league:list:master";

    private static final Duration LEAGUE_LIST_TTL = Duration.ofHours(24);

    @Transactional(readOnly = true)
    public LeagueListDto getChallengerLeagueList() {
        return getLeagueListFromCacheOrDb(
                CHALLENGER_CACHE_KEY,
                CHALLENGER_LEAGUE_ID,
                () -> leagueListRepository.findByLeagueId(CHALLENGER_LEAGUE_ID)
        );
    }

    @Transactional(readOnly = true)
    public LeagueListDto getGrandmasterLeagueList() {
        return getLeagueListFromCacheOrDb(
                GRANDMASTER_CACHE_KEY,
                GRANDMASTER_LEAGUE_ID,
                () -> leagueListRepository.findByLeagueId(GRANDMASTER_LEAGUE_ID)
        );
    }

    @Transactional(readOnly = true)
    public LeagueListDto getMasterLeagueList() {
        return getLeagueListFromCacheOrDb(
                MASTER_CACHE_KEY,
                MASTER_LEAGUE_ID,
                () -> leagueListRepository.findByLeagueId(MASTER_LEAGUE_ID)
        );
    }

    private LeagueListDto getLeagueListFromCacheOrDb(String cacheKey, String leagueIdForFallback, Supplier<Optional<LeagueList>> dbFetcher) {
        return leagueListCacheService.getOrLoad(
                cacheKey,
                () -> dbFetcher.get()
                        .map(leagueListMapper::toDto)
                        .orElseGet(() -> {
                            log.warn("League list not found in DB key: {}", cacheKey);
                            throw new LeagueListNotFoundException("League list data not found " + leagueIdForFallback);
                        }),
                LEAGUE_LIST_TTL
        );
    }

    @Transactional
    public LeagueListDto refreshChallengerLeague() {
        log.info("Refreshing Challenger league list");
        return refreshLeagueListInternal(
                CHALLENGER_LEAGUE_ID,
                "CHALLENGER",
                LeagueQueue.RANKED_SOLO_5x5.getQueue(),
                () -> apiService.getChallengerLeagueList(LeagueQueue.RANKED_SOLO_5x5.getQueue()),
                CHALLENGER_CACHE_KEY
        );
    }

    @Transactional
    public LeagueListDto refreshGrandmasterLeague() {
        log.info("Refreshing Grandmaster league list");
        return refreshLeagueListInternal(
                GRANDMASTER_LEAGUE_ID,
                "GRANDMASTER",
                LeagueQueue.RANKED_SOLO_5x5.getQueue(),
                () -> apiService.getGrandmasterLeagueList(LeagueQueue.RANKED_SOLO_5x5.getQueue()),
                GRANDMASTER_CACHE_KEY
        );
    }

    @Transactional
    public LeagueListDto refreshMasterLeague() {
        log.info("Refreshing Master league list");
        return refreshLeagueListInternal(
                MASTER_LEAGUE_ID,
                "Master",
                LeagueQueue.RANKED_SOLO_5x5.getQueue(),
                () -> apiService.getMasterLeagueList(LeagueQueue.RANKED_SOLO_5x5.getQueue()),
                MASTER_CACHE_KEY
        );
    }

    private LeagueListDto refreshLeagueListInternal(String leagueId,
                                                    String tier,
                                                    String queue,
                                                    Supplier<Optional<LeagueListDto>> apiFetcher,
                                                    String cacheKey) {
        LeagueListDto apiLeagueListDto = apiFetcher.get()
                .orElseThrow(() -> {
                    log.error("Failed fetch league list tier: {}", tier);
                    return new LeagueListNotFoundException("data not found for " + tier);
                });

        if (apiLeagueListDto.getLeagueId() == null) {
            apiLeagueListDto.setLeagueId(leagueId);
        }
        if (apiLeagueListDto.getTier() == null) {
            apiLeagueListDto.setTier(tier);
        }
        if (apiLeagueListDto.getQueue() == null) {
            apiLeagueListDto.setQueue(queue);
        }


        LeagueList leagueListEntity = leagueListRepository.findByLeagueId(leagueId)
                .orElseGet(() -> {
                    log.info("new LeagueList entity leagueId: {}", leagueId);

                    LeagueList leagueList = new LeagueList();
                    leagueList.setLeagueId(leagueId);
                    leagueList.setTier(apiLeagueListDto.getTier());
                    leagueList.setName(apiLeagueListDto.getLeagueId());
                    leagueList.setQueue(apiLeagueListDto.getQueue());
                    leagueList.setEntries(new ArrayList<>());

                    return leagueList;
                });

        leagueListEntity.setTier(apiLeagueListDto.getTier());
        leagueListEntity.setName(apiLeagueListDto.getName());
        leagueListEntity.setQueue(apiLeagueListDto.getQueue());

        Map<String, LeagueItem> existingItemsBySummonerId = leagueListEntity.getEntries().stream()
                .collect(Collectors.toMap(LeagueItem::getSummonerId, item -> item));

        Set<String> apiSummonerIds = new HashSet<>();
        List<LeagueItem> updatedOrNewItems = new ArrayList<>();

        for (LeagueItemDto itemDto : apiLeagueListDto.getEntries()) {
            apiSummonerIds.add(itemDto.getSummonerId());
            LeagueItem existingItem = existingItemsBySummonerId.get(itemDto.getSummonerId());
            if (existingItem != null) {
                existingItem.update(itemDto);
                updatedOrNewItems.add(existingItem);
            } else {
                LeagueItem newItem = leagueItemMapper.toEntity(itemDto, leagueListEntity);
                updatedOrNewItems.add(newItem);
            }
        }

        leagueListEntity.getEntries().clear();
        leagueListEntity.getEntries().addAll(updatedOrNewItems);

        LeagueList savedLeagueList = leagueListRepository.save(leagueListEntity);
        log.info("Saved LeagueList leagueId: {}, {} entries.", savedLeagueList.getLeagueId(), savedLeagueList.getEntries().size());

        LeagueListDto updatedDto = leagueListMapper.toDto(savedLeagueList);

        leagueListCacheService.put(cacheKey, updatedDto, LEAGUE_LIST_TTL);
        log.info("League list cache updated key: {}", cacheKey);

        return updatedDto;
    }

    @Transactional
    @PostConstruct
    public void initLeagueLists() {
        log.info("Initial league lists");
        try {
            refreshChallengerLeague();
        } catch (Exception e) {
            log.error("Failed initialize Challenger league: {}", e.getMessage());
        }
        try {
            refreshGrandmasterLeague();
        } catch (Exception e) {
            log.error("Failed initialize Grandmaster league: {}", e.getMessage());
        }
        try {
//            refreshMasterLeague();
        } catch (Exception e) {
            log.error("Failed initialize Master league: {}", e.getMessage());
        }
    }

    @Scheduled(cron = "0 0 21 * * ?")
    @Transactional
    public void scheduleRefreshChallengerLeague() {
        log.info("Scheduled: Refreshing Challenger league list");
        refreshChallengerLeague();
    }

    @Scheduled(cron = "0 20 21 * * ?")
    @Transactional
    public void scheduleRefreshGrandmasterLeague() {
        log.info("Scheduled: Refreshing Grandmaster league list");
        refreshGrandmasterLeague();
    }

    @Scheduled(cron = "0 40 21 * * ?")
    @Transactional
    public void scheduleRefreshMasterLeague() {
        log.info("Scheduled: Refreshing Master league list");
        refreshMasterLeague();
    }
}
