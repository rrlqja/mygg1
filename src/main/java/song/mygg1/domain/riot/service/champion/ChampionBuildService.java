package song.mygg1.domain.riot.service.champion;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import song.mygg1.domain.redis.service.CacheService;
import song.mygg1.domain.riot.dto.champion.ChampionLevelSkillStatsResponse;
import song.mygg1.domain.riot.dto.champion.ChampionSkillTreeDto;
import song.mygg1.domain.riot.dto.champion.LevelSkillData;
import song.mygg1.domain.riot.dto.match.championbuild.AggregatedCoreItemStatsDto;
import song.mygg1.domain.riot.dto.match.championbuild.CoreItemStatDto;
import song.mygg1.domain.riot.dto.match.championbuild.ItemStatCounter;
import song.mygg1.domain.riot.dto.match.championbuild.MatchPlayerInfo;
import song.mygg1.domain.riot.entity.item.Item;
import song.mygg1.domain.riot.entity.timeline.events.ItemPurchaseEvent;
import song.mygg1.domain.riot.entity.timeline.events.SkillLevelUpEvent;
import song.mygg1.domain.riot.repository.item.ItemJpaRepository;
import song.mygg1.domain.riot.repository.match.MatchJpaRepository;
import song.mygg1.domain.riot.repository.timeline.ItemPurchaseEventJpaRepository;
import song.mygg1.domain.riot.repository.timeline.LevelUpEventJpaRepository;
import song.mygg1.domain.riot.repository.timeline.SkillLevelUpEventJpaRepository;
import song.mygg1.domain.riot.repository.timeline.TimeLineJpaRepository;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
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
    private final CacheService<AggregatedCoreItemStatsDto> itemBuildCacheService;
    private final CacheService<ChampionSkillTreeDto> skillTreeCacheService;
    private final MatchJpaRepository matchRepository;
    private final TimeLineJpaRepository timeLineRepository;
    private final ItemPurchaseEventJpaRepository itemPurchaseEventRepository;
    private final SkillLevelUpEventJpaRepository sKillLevelUpEventRepository;
    private final LevelUpEventJpaRepository levelUpEventRepository;

    private final ItemJpaRepository itemRepository;

    private static final ZoneId kst = ZoneId.of("Asia/Seoul");
    private static final Duration ITEM_BUILD_TTL = Duration.ofDays(7);
    private static final Duration SKILL_TREE_TTL = Duration.ofDays(7);
    private static final int FIRST_CORE_LIMIT = 3;
    private static final int SECOND_CORE_LIMIT = 3;
    private static final int THIRD_CORE_LIMIT = 5;
    private static final int MAX_QWE_POINTS = 5;
    private static final int MAX_R_POINTS = 3;
    private static final int MAX_CHAMPION_LEVEL = 18;

//    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    public ChampionSkillTreeDto getChampionSkillTree(Integer championId) {
//        String key = "champion:build:skill:" + championId;
//
//        skillTreeCacheService.getOrLoad(
//                key,
//                () -> buildSkillTree(championId),
//                SKILL_TREE_TTL
//        );
//
//        return buildSkillTree(championId);
//    }

    @Transactional
    public ChampionLevelSkillStatsResponse buildSkillTree(Integer championId) {
        LocalDate today = LocalDate.now(kst);
        ZonedDateTime endZonedDateTime = today.atStartOfDay(kst);
        Instant endInstant = endZonedDateTime.toInstant();
        long endTimestamp = endInstant.toEpochMilli();

        LocalDate startDateValue = today.minusMonths(3).withDayOfMonth(1);
        ZonedDateTime startZonedDateTime = startDateValue.atStartOfDay(kst);
        Instant startInstant = startZonedDateTime.toInstant();
        long startTimestamp = startInstant.toEpochMilli();

        List<MatchPlayerInfo> targetPlayers = matchRepository.findMatchPlayerInfoByChampionAndPeriod(championId, startTimestamp, endTimestamp, PageRequest.of(0, 1500));
        log.info("Found {} matches to analyze.", targetPlayers.size());

        Map<Integer, Map<Integer, Integer>> skillTreeFrequency = new HashMap<>();
        for (int level = 1; level <= 18; level++) {
            Map<Integer, Integer> skillCountsForLevel = new HashMap<>();

            for(int slot = 1; slot <= 4; slot++) skillCountsForLevel.put(slot, 0);
            skillTreeFrequency.put(level, skillCountsForLevel);
        }
        int analyzedMatchCount = 0;

        for (MatchPlayerInfo matchInfo : targetPlayers) {
            log.info("Analyzing matchId: {}, participantId: {}", matchInfo.getMatchId(), matchInfo.getParticipantId());
            List<SkillLevelUpEvent> skillEvents = sKillLevelUpEventRepository.findByMatchIdsInAndLevelUpType(
                    matchInfo.getMatchId(),
                    matchInfo.getParticipantId()
            );
//            log.info("For matchId: {}, participantId: {}, found {} skill events.",
//                    matchInfo.getMatchId(), matchInfo.getParticipantId(), skillEvents.size());

            if (!skillEvents.isEmpty()) {
                analyzedMatchCount++;
                for (int i = 0; i < skillEvents.size(); i++) {
                    int currentSkillLevelOrder = i + 1;
                    if (currentSkillLevelOrder > 18) break;

                    SkillLevelUpEvent event = skillEvents.get(i);
                    int skillSlot = event.getSkillSlot();


                    skillTreeFrequency.get(currentSkillLevelOrder).merge(skillSlot, 1, Integer::sum);
                }
                log.debug("Skill sequence for matchId {}: {}", matchInfo.getMatchId(), analyzedMatchCount);
            } else {
                log.debug("No SKILL_LEVEL_UP events found for matchId: {} and participantId: {}", matchInfo.getMatchId(), matchInfo.getParticipantId());
            }
        }

        if (skillTreeFrequency.isEmpty()) {
            log.info("No skill sequences found after analyzing {} matches.", targetPlayers.size());
            return new ChampionLevelSkillStatsResponse(championId, Collections.emptyList(), 0);
        }

        List<LevelSkillData> resultLevelStats = new ArrayList<>();
        for (int level = 1; level <= 18; level++) {
            Map<Integer, Integer> countsAtThisLevelOrder = skillTreeFrequency.get(level);
            int totalPicksAtThisLevelOrder = countsAtThisLevelOrder.values().stream().mapToInt(Integer::intValue).sum();

            Map<String, Double> percentages = new HashMap<>();
            String mostPicked = "N/A";
            int maxPicks = -1;

            if (totalPicksAtThisLevelOrder > 0) {
                for (int slot = 1; slot <= 4; slot++) {
                    int count = countsAtThisLevelOrder.getOrDefault(slot, 0);
                    percentages.put(skillSlotToString(slot), (double) count * 100.0 / totalPicksAtThisLevelOrder);
                    if (count > maxPicks) {
                        maxPicks = count;
                        mostPicked = skillSlotToString(slot);
                    } else if (count == maxPicks && maxPicks != 0) {
                        mostPicked += "/" + skillSlotToString(slot);
                    }
                }
            } else {
                for (int slot = 1; slot <= 4; slot++) percentages.put(skillSlotToString(slot), 0.0);
            }
            resultLevelStats.add(new LevelSkillData(level, percentages, mostPicked, totalPicksAtThisLevelOrder));
        }

        log.info("Successfully calculated level-based skill stats for championId: {}", championId);
        return new ChampionLevelSkillStatsResponse(championId, resultLevelStats, analyzedMatchCount);
    }

    private String skillSlotToString(int skillSlot) {
        return switch (skillSlot) {
            case 1 -> "Q";
            case 2 -> "W";
            case 3 -> "E";
            case 4 -> "R";
            default -> "";
        };
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public AggregatedCoreItemStatsDto getChampionItemBuild(Integer championId) {
        String key = "champion:build:item:" + championId;

        AggregatedCoreItemStatsDto dto = itemBuildCacheService.getOrLoad(
                key,
                () -> setChampionItemBuild(championId),
                ITEM_BUILD_TTL
        );

        return dto;
    }

    public AggregatedCoreItemStatsDto setChampionItemBuild(Integer championId) {
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
        Map<Integer, String> coreItemNameMap = coreItemList.stream()
                .collect(Collectors.toMap(
                        item -> Integer.parseInt(item.getId()),
                        Item::getName,
                        (name1, name2) -> name1
                ));

        List<MatchPlayerInfo> infoList = matchRepository.findMatchPlayerInfoByChampionAndPeriod(championId, start, end, PageRequest.of(0, 3000));
        List<String> matchIds = infoList.stream().map(MatchPlayerInfo::getMatchId).distinct().toList();
        List<ItemPurchaseEvent> allPurchaseEvents = itemPurchaseEventRepository.findByMatchIds(matchIds);

        Map<String, Map<Integer, List<ItemPurchaseEvent>>> groupedEvents = allPurchaseEvents.stream()
                .collect(Collectors.groupingBy(
                        event -> event.getFrame().getId().getMatchId(),
                        Collectors.groupingBy(
                                ItemPurchaseEvent::getParticipantId,
                                Collectors.toList()
                        )
                ));

        Map<Integer, ItemStatCounter> firstCoreItemCounters = new HashMap<>();
        Map<Integer, ItemStatCounter> secondCoreItemCounters = new HashMap<>();
        Map<Integer, ItemStatCounter> thirdCoreItemCounters = new HashMap<>();

        int gamesWithFirstCore = 0;
        int gamesWithSecondCore = 0;
        int gamesWithThirdCore = 0;

        for (MatchPlayerInfo playerInfo : infoList) {
            List<ItemPurchaseEvent> purchaseEvents = groupedEvents
                    .getOrDefault(playerInfo.getMatchId(), Collections.emptyMap())
                    .getOrDefault(playerInfo.getParticipantId(), Collections.emptyList());

            purchaseEvents.sort(Comparator.comparing(ItemPurchaseEvent::getTimestamp));

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

        List<CoreItemStatDto> firstCoreResult = formatResults(firstCoreItemCounters, gamesWithFirstCore, coreItemNameMap, FIRST_CORE_LIMIT);
        List<CoreItemStatDto> secondCoreResult = formatResults(secondCoreItemCounters, gamesWithSecondCore, coreItemNameMap, SECOND_CORE_LIMIT);
        List<CoreItemStatDto> thirdCoreResult = formatResults(thirdCoreItemCounters, gamesWithThirdCore, coreItemNameMap, THIRD_CORE_LIMIT);

        return new AggregatedCoreItemStatsDto(firstCoreResult, secondCoreResult, thirdCoreResult);
    }

    private List<CoreItemStatDto> formatResults(Map<Integer, ItemStatCounter> counters, int totalGamesForSlot, Map<Integer, String> coreItemMap, Integer limit) {
        if (totalGamesForSlot == 0) {
            return Collections.emptyList();
        }

        List<CoreItemStatDto> resultList = counters.entrySet().stream()
                .map(entry -> {
                    int itemId = entry.getKey();
                    ItemStatCounter counter = entry.getValue();
                    String itemName = coreItemMap.getOrDefault(itemId, "아이템명 오류: " + itemId);

                    double purchasePercentage = Math.floor((double) counter.getPurchaseCount() / totalGamesForSlot * 100.0 * 100) / 100;
                    double winRate = Math.floor(((counter.getPurchaseCount() > 0) ? (double) counter.getWinCount() / counter.getPurchaseCount() * 100.0 : 0.0) * 100) / 100;
                    return new CoreItemStatDto(itemId, itemName, purchasePercentage, winRate, counter.getPurchaseCount());
                })
                .sorted(Comparator.comparing(CoreItemStatDto::getPurchasePercentage).reversed())
                .toList();

        int actualLimit = Math.min(limit, resultList.size());
        if (actualLimit > 0 || !resultList.isEmpty()) {
            return resultList.subList(0, actualLimit);
        }
        return Collections.emptyList();
    }
}
