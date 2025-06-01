package song.mygg1.domain.riot.service.champion;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import song.mygg1.domain.common.exception.riot.champion.exceptons.ChampionNotFoundException;
import song.mygg1.domain.redis.service.CacheService;
import song.mygg1.domain.riot.dto.champion.ChampionBanPickDto;
import song.mygg1.domain.riot.entity.champion.Champion;
import song.mygg1.domain.riot.repository.champion.ChampionJpaRepository;
import song.mygg1.domain.riot.repository.match.BanJpaRepository;
import song.mygg1.domain.riot.repository.match.InfoJpaRepository;
import song.mygg1.domain.riot.repository.match.ParticipantJpaRepository;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChampionStatsService {
    private final CacheService<ChampionBanPickDto> cacheService;
    private final ChampionJpaRepository championRepository;
    private final BanJpaRepository banRepository;
    private final InfoJpaRepository infoRepository;
    private final ParticipantJpaRepository participantRepository;

    private static final ZoneId kst = ZoneId.of("Asia/Seoul");

    private static final Duration STATS_TTL = Duration.ofDays(1);
    private static final String CHAMPION_STATS_CACHE_KEY_PREFIX = "champion:build:stats:";

    @Transactional(readOnly = true)
    public List<ChampionBanPickDto> getAllChampionStats() {
        List<Champion> championList = championRepository.findAll();
        List<ChampionBanPickDto> championStatsList = new ArrayList<>();

        for (Champion champion : championList) {
            try {
                ChampionBanPickDto statsDto = getChampionStats(champion.getKey());
                if (statsDto != null) {
                    championStatsList.add(statsDto);
                }
            } catch (Exception e) {
                log.error("loading error champion {} getAllChampionStatsRanked: {}", champion.getKey(), e.getMessage());
            }
        }
        championStatsList.sort((dto1, dto2) -> {
            double score1 = calculateChampionScore(dto1);
            double score2 = calculateChampionScore(dto2);
            return Double.compare(score2, score1);
        });

        int totalChampionsInList = championStatsList.size();
        for (int i = 0; i < totalChampionsInList; i++) {
            ChampionBanPickDto dto = championStatsList.get(i);
            if (dto.getPickRate() < 0.5 && totalChampionsInList > 20) {
                dto.setTier("D");
                continue;
            }

            String tier;
            if (i < totalChampionsInList * 0.07) tier = "S";
            else if (i < totalChampionsInList * 0.20) tier = "A";
            else if (i < totalChampionsInList * 0.45) tier = "B";
            else if (i < totalChampionsInList * 0.70) tier = "C";
            else tier = "D";
            dto.setTier(tier);
        }

        return championStatsList;
    }

    private double calculateChampionScore(ChampionBanPickDto dto) {
        if (dto.getTotalPicked() == null || dto.getTotalPicked() == 0) {
            return 0.0;
        }

        if (dto.getPickRate() < 1.0) {
            return dto.getWinRate() * 0.2;
        }

        double mainScore = (dto.getWinRate() * 0.65) + (dto.getPickRate() * 0.25);

        double banBonus = Math.min(dto.getBanRate(), 30.0) * 0.10;

        return mainScore + banBonus;
    }

    @Scheduled(cron = "0 0 21 * * ?")
    @Transactional
    public void refreshAllChampionStatsCache() {
        log.info("refresh all champion stats cache");
        List<Champion> championList = championRepository.findAll();

        LocalDate yesterday = LocalDate.now(kst).minusDays(1);
        LocalDate startDate = yesterday.minusDays(6);
        long startTimestamp = startDate.atStartOfDay(kst).toInstant().toEpochMilli();
        long endTimestamp = yesterday.atTime(LocalTime.MAX).atZone(kst).toInstant().toEpochMilli();
        Long totalGamesInPeriod = infoRepository.countTotalGamesInGameCreation(startTimestamp, endTimestamp);

        if (totalGamesInPeriod == null || totalGamesInPeriod == 0) {
            log.warn("No game Evicting all champion stats");
            championList.forEach(champion -> cacheService.evict(CHAMPION_STATS_CACHE_KEY_PREFIX + champion.getKey()));
            return;
        }

        int successCount = 0;
        int failureCount = 0;

        for (Champion champion : championList) {
            String cacheKey = CHAMPION_STATS_CACHE_KEY_PREFIX + champion.getKey();
            try {
                cacheService.evict(cacheKey);
                ChampionBanPickDto newStats = calculateChampionStats(champion.getKey(), champion.getName(), totalGamesInPeriod, startTimestamp, endTimestamp);
                cacheService.put(cacheKey, newStats, STATS_TTL);

                successCount++;
            } catch (Exception e) {
                failureCount++;
                log.error("Error refreshing stats champion {}: {}", champion.getKey(), e.getMessage(), e);
            }
        }
        log.info("Finished refreshing champion stats Success: {}, Failure: {}", successCount, failureCount);
    }

    public ChampionBanPickDto getChampionStats(Long championId) {
        String cacheKey = CHAMPION_STATS_CACHE_KEY_PREFIX + championId;

        return cacheService.getOrLoad(
                cacheKey,
                () -> {
                    Champion champion = championRepository.findChampionByKey(championId)
                            .orElseThrow(() -> new ChampionNotFoundException("Champion not found with id: " + championId));

                    LocalDate yesterday = LocalDate.now(kst).minusDays(1);
                    LocalDate startDate = yesterday.minusDays(6);
                    long startTimestamp = startDate.atStartOfDay(kst).toInstant().toEpochMilli();
                    long endTimestamp = yesterday.atTime(LocalTime.MAX).atZone(kst).toInstant().toEpochMilli();

                    Long totalGamesInPeriod = infoRepository.countTotalGamesInGameCreation(startTimestamp, endTimestamp);

                    return calculateChampionStats(champion.getKey(), champion.getName(), totalGamesInPeriod, startTimestamp, endTimestamp);
                },
                STATS_TTL
        );
    }

    private ChampionBanPickDto calculateChampionStats(Long championId, String championName, Long totalGamesInPeriod, long startTimestamp, long endTimestamp) {
        if (totalGamesInPeriod == null || totalGamesInPeriod == 0) {
            return new ChampionBanPickDto(championId, championName, 0L, 0L, 0L, 0L, 0.0, 0.0, 0.0, "N/A");
        }

        Long banCount = banRepository.countBanChampionInGameCreation(championId.intValue(), startTimestamp, endTimestamp);
        if (banCount == null) banCount = 0L;

        Long pickCount = participantRepository.countPickedChampionInGameCreation(championId.intValue(), startTimestamp, endTimestamp);
        if (pickCount == null) pickCount = 0L;

        Long winCount = 0L;
        if (pickCount > 0) {
            winCount = participantRepository.countWinChampionInGameCreation(championId.intValue(), startTimestamp, endTimestamp);
            if (winCount == null) winCount = 0L;
        }

        double banRate = Math.floor(((double) banCount / totalGamesInPeriod) * 100.0 * 100.0) / 100.0;
        double pickRate = Math.floor(((double) pickCount / totalGamesInPeriod) * 100.0 * 100.0) / 100.0;
        double winRate = 0.0;
        if (pickCount > 0) {
            winRate = Math.floor(((double) winCount / pickCount) * 100.0 * 100.0) / 100.0;
        }

        return new ChampionBanPickDto(championId, championName, totalGamesInPeriod, banCount, pickCount, winCount, banRate, pickRate, winRate);
    }
}
