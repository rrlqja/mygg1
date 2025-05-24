package song.mygg1.domain.riot.service.champion;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import song.mygg1.domain.redis.service.CacheService;
import song.mygg1.domain.riot.dto.match.championbuild.AggregatedCoreItemStatsDto;
import song.mygg1.domain.riot.dto.match.championbuild.CoreItemStatDto;
import song.mygg1.domain.riot.dto.match.championbuild.ItemStatCounter;
import song.mygg1.domain.riot.dto.match.championbuild.MatchPlayerInfo;
import song.mygg1.domain.riot.entity.item.Item;
import song.mygg1.domain.riot.entity.timeline.events.ItemPurchaseEvent;
import song.mygg1.domain.riot.repository.item.ItemJpaRepository;
import song.mygg1.domain.riot.repository.match.MatchJpaRepository;
import song.mygg1.domain.riot.repository.timeline.ItemPurchaseEventJpaRepository;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChampionBuildService {
    private final CacheService<AggregatedCoreItemStatsDto> cacheService;
    private final MatchJpaRepository matchRepository;
    private final ItemPurchaseEventJpaRepository itemPurchaseEventRepository;
    private final ItemJpaRepository itemRepository;

    private static final ZoneId kst = ZoneId.of("Asia/Seoul");
    private static final Duration MATCH_TIMELINE_TTL = Duration.ofDays(7);

    public AggregatedCoreItemStatsDto getChampionItemBuild(Integer championId) {
        String key = "champion:build:item:" + championId;

        AggregatedCoreItemStatsDto dto = cacheService.getOrLoad(
                key,
                () -> setChampionItemBuild(championId),
                MATCH_TIMELINE_TTL
        );

        return dto;
    }

    private AggregatedCoreItemStatsDto setChampionItemBuild(Integer championId) {
        LocalDate today = LocalDate.now(kst);
        ZonedDateTime endZonedDateTime = today.atStartOfDay(kst);
        Instant endInstant = endZonedDateTime.toInstant();
        long end = endInstant.toEpochMilli();

        LocalDate startDateValue = today.minusMonths(3).withDayOfMonth(1);
        ZonedDateTime startZonedDateTime = startDateValue.atStartOfDay(kst);
        Instant startInstant = startZonedDateTime.toInstant();
        long start = startInstant.toEpochMilli();

        List<Item> coreItemList = itemRepository.findCoreItemList();
        List<Integer> coreItemIdList = coreItemList.stream()
                .map(i -> Integer.valueOf(i.getId()))
                .toList();

        List<MatchPlayerInfo> infoList = matchRepository.findMatchPlayerInfoByChampionAndPeriod(championId, start, end);

        Map<Integer, ItemStatCounter> firstCoreItemCounters = new HashMap<>();
        Map<Integer, ItemStatCounter> secondCoreItemCounters = new HashMap<>();
        Map<Integer, ItemStatCounter> thirdCoreItemCounters = new HashMap<>();

        int gamesWithFirstCore = 0;
        int gamesWithSecondCore = 0;
        int gamesWithThirdCore = 0;

        for (MatchPlayerInfo playerInfo : infoList) {
            List<ItemPurchaseEvent> purchaseEvents = itemPurchaseEventRepository
                    .findByMatchIdAndParticipantIdOrderByTimestampAsc(playerInfo.getMatchId(), playerInfo.getParticipantId());

            List<Integer> purchasedCoreItemsInOrder = purchaseEvents.stream()
                    .map(ItemPurchaseEvent::getItemId)
                    .filter(coreItemIdList::contains)
                    .toList();

            if (!purchasedCoreItemsInOrder.isEmpty()) {
                gamesWithFirstCore++;
                int firstCoreItemId = purchasedCoreItemsInOrder.get(0);
                ItemStatCounter firstCounter = firstCoreItemCounters.computeIfAbsent(firstCoreItemId, k -> new ItemStatCounter());
                firstCounter.incrementPurchase();
                if (playerInfo.getWin()) {
                    firstCounter.incrementWin();
                }

                if (purchasedCoreItemsInOrder.size() >= 2) {
                    gamesWithSecondCore++;
                    int secondCoreItemId = purchasedCoreItemsInOrder.get(1);
                    ItemStatCounter secondCounter = secondCoreItemCounters.computeIfAbsent(secondCoreItemId, k -> new ItemStatCounter());
                    secondCounter.incrementPurchase();
                    if (playerInfo.getWin()) {
                        secondCounter.incrementWin();
                    }

                    if (purchasedCoreItemsInOrder.size() >= 3) {
                        gamesWithThirdCore++;
                        int thirdCoreItemId = purchasedCoreItemsInOrder.get(2);
                        ItemStatCounter thirdCounter = thirdCoreItemCounters.computeIfAbsent(thirdCoreItemId, k -> new ItemStatCounter());
                        thirdCounter.incrementPurchase();
                        if (playerInfo.getWin()) {
                            thirdCounter.incrementWin();
                        }
                    }
                }
            }
        }

        List<CoreItemStatDto> firstCoreResult = formatResults(firstCoreItemCounters, gamesWithFirstCore, coreItemList);
        List<CoreItemStatDto> secondCoreResult = formatResults(secondCoreItemCounters, gamesWithSecondCore, coreItemList);
        List<CoreItemStatDto> thirdCoreResult = formatResults(thirdCoreItemCounters, gamesWithThirdCore, coreItemList);

        return new AggregatedCoreItemStatsDto(firstCoreResult, secondCoreResult, thirdCoreResult);
    }

    private List<CoreItemStatDto> formatResults(Map<Integer, ItemStatCounter> counters, int totalGamesForSlot, List<Item> coreItemList) {
        if (totalGamesForSlot == 0) {
            return Collections.emptyList();
        }

        Map<Integer, String> itemMap = coreItemList.stream()
                .collect(Collectors.toMap(item -> Integer.valueOf(item.getId()), Item::getName, (name1, name2) -> name1));

        return counters.entrySet().stream()
                .map(entry -> {
                    int itemId = entry.getKey();
                    ItemStatCounter counter = entry.getValue();
                    String itemName = itemMap.getOrDefault(itemId, "아이템명 오류: " + itemId);
                    double purchasePercentage = (double) counter.getPurchaseCount() / totalGamesForSlot * 100.0;
                    double winRate = (counter.getPurchaseCount() > 0) ? (double) counter.getWinCount() / counter.getPurchaseCount() * 100.0 : 0.0;
                    return new CoreItemStatDto(itemId, itemName, purchasePercentage, winRate, counter.getPurchaseCount());
                })
                .sorted(Comparator.comparing(CoreItemStatDto::getPurchasePercentage).reversed())
                .toList();
    }
}
