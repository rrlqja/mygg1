package song.mygg1.domain.riot.service.champion;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import song.mygg1.domain.redis.service.CacheService;
import song.mygg1.domain.riot.dto.champion.ChampionSkillTreeDto;
import song.mygg1.domain.riot.dto.champion.SkillSlotCounter;
import song.mygg1.domain.riot.dto.match.championbuild.AggregatedCoreItemStatsDto;
import song.mygg1.domain.riot.dto.match.championbuild.CoreItemStatDto;
import song.mygg1.domain.riot.dto.match.championbuild.ItemStatCounter;
import song.mygg1.domain.riot.dto.match.championbuild.MatchPlayerInfo;
import song.mygg1.domain.riot.entity.item.Item;
import song.mygg1.domain.riot.entity.timeline.EventTimeLine;
import song.mygg1.domain.riot.entity.timeline.FrameTimeLine;
import song.mygg1.domain.riot.entity.timeline.ParticipantFrame;
import song.mygg1.domain.riot.entity.timeline.Timeline;
import song.mygg1.domain.riot.entity.timeline.events.ItemPurchaseEvent;
import song.mygg1.domain.riot.entity.timeline.events.SkillLevelUpEvent;
import song.mygg1.domain.riot.repository.item.ItemJpaRepository;
import song.mygg1.domain.riot.repository.match.MatchJpaRepository;
import song.mygg1.domain.riot.repository.timeline.ItemPurchaseEventJpaRepository;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    private final ItemJpaRepository itemRepository;

    private static final ZoneId kst = ZoneId.of("Asia/Seoul");
    private static final Duration ITEM_BUILD_TTL = Duration.ofDays(7);
    private static final Duration SKILL_TREE_TTL = Duration.ofDays(7);
    private static final int FIRST_CORE_LIMIT = 3;
    private static final int SECOND_CORE_LIMIT = 3;
    private static final int THIRD_CORE_LIMIT = 5;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ChampionSkillTreeDto getChampionSkillTree(Integer championId) {
        String key = "champion:build:skill:" + championId;

        skillTreeCacheService.getOrLoad(
                key,
                () -> buildSkillTree(championId),
                SKILL_TREE_TTL
        );

        return buildSkillTree(championId);
    }

    @Transactional
    public ChampionSkillTreeDto buildSkillTree(Integer championId) {
        LocalDate today = LocalDate.now(kst);
        ZonedDateTime endZonedDateTime = today.atStartOfDay(kst);
        Instant endInstant = endZonedDateTime.toInstant();
        long endTimestamp = endInstant.toEpochMilli();

        LocalDate startDateValue = today.minusMonths(3).withDayOfMonth(1);
        ZonedDateTime startZonedDateTime = startDateValue.atStartOfDay(kst);
        Instant startInstant = startZonedDateTime.toInstant();
        long startTimestamp = startInstant.toEpochMilli();

        List<MatchPlayerInfo> targetPlayers = matchRepository.findMatchPlayerInfoByChampionAndPeriod(championId, startTimestamp, endTimestamp, PageRequest.of(0, 1500));

        Map<Integer, SkillSlotCounter> aggregatedSkillCountsByLevel = new HashMap<>();
        for (int i = 1; i <= 18; i++) {
            aggregatedSkillCountsByLevel.put(i, new SkillSlotCounter());
        }

        for (MatchPlayerInfo playerInfo : targetPlayers) {
            Timeline timeline = timeLineRepository.findById(playerInfo.getMatchId()).orElse(null);
            if (timeline == null || timeline.getInfo() == null || timeline.getInfo().getFrames() == null) {
                log.warn("타임라인 정보를 찾을 수 없습니다: {}", playerInfo.getMatchId());
                continue;
            }

            Set<String> skillUpProcessedForLevelInGame = new HashSet<>();

            List<FrameTimeLine> frames = timeline.getInfo().getFrames();
            frames.sort(Comparator.comparing(frame -> frame.getId().getTimestamp()));

            for (FrameTimeLine frame : frames) {
                if (frame.getEvents() == null) continue;

                List<EventTimeLine> frameEvents = new ArrayList<>(frame.getEvents());
                frameEvents.sort(Comparator.comparing(EventTimeLine::getTimestamp));

                for (EventTimeLine event : frameEvents) {
                    if (event instanceof SkillLevelUpEvent suEvent &&
                            suEvent.getParticipantId() != null &&
                            suEvent.getParticipantId().equals(playerInfo.getParticipantId()) &&
                            "NORMAL".equalsIgnoreCase(suEvent.getLevelUpType())) {

                        Integer championLevelAtEvent = null;
                        if (frame.getParticipantFrames() != null) {
                            for (ParticipantFrame pf : frame.getParticipantFrames()) {
                                if (pf.getId() != null && Integer.valueOf(pf.getId().getParticipantId()).equals(suEvent.getParticipantId())) {
                                    championLevelAtEvent = pf.getLevel();
                                    break;
                                }
                            }
                        }

                        if (championLevelAtEvent != null && championLevelAtEvent > 0 && championLevelAtEvent <= 18) {
                            String levelSkillKey = championLevelAtEvent + "-" + suEvent.getSkillSlot();
                            if (!skillUpProcessedForLevelInGame.contains(levelSkillKey)) {
                                aggregatedSkillCountsByLevel.get(championLevelAtEvent).incrementSkill(suEvent.getSkillSlot());
                                skillUpProcessedForLevelInGame.add(levelSkillKey);
                            }
                        }
                    }
                }
            }
        }
        List<String> finalSkillTree = new ArrayList<>();
        for (int level = 1; level <= 18; level++) {
            SkillSlotCounter counter = aggregatedSkillCountsByLevel.get(level);
            int mostPickedSlot = 0;
            if (counter != null) {
                mostPickedSlot = counter.getMostPickedSkillSlot();
            }
            finalSkillTree.add(skillSlotToString(mostPickedSlot));
        }

        return new ChampionSkillTreeDto(finalSkillTree);
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

        List<MatchPlayerInfo> infoList = matchRepository.findMatchPlayerInfoByChampionAndPeriod(championId, start, end, PageRequest.of(0, 3000));

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

        List<CoreItemStatDto> firstCoreResult = formatResults(firstCoreItemCounters, gamesWithFirstCore, coreItemList, FIRST_CORE_LIMIT);
        List<CoreItemStatDto> secondCoreResult = formatResults(secondCoreItemCounters, gamesWithSecondCore, coreItemList, SECOND_CORE_LIMIT);
        List<CoreItemStatDto> thirdCoreResult = formatResults(thirdCoreItemCounters, gamesWithThirdCore, coreItemList, THIRD_CORE_LIMIT);

        return new AggregatedCoreItemStatsDto(firstCoreResult, secondCoreResult, thirdCoreResult);
    }

    private List<CoreItemStatDto> formatResults(Map<Integer, ItemStatCounter> counters, int totalGamesForSlot, List<Item> coreItemList, Integer limit) {
        if (totalGamesForSlot == 0) {
            return Collections.emptyList();
        }

        Map<Integer, String> itemMap = coreItemList.stream()
                .collect(Collectors.toMap(item -> Integer.valueOf(item.getId()), Item::getName, (name1, name2) -> name1));

        List<CoreItemStatDto> resultList = counters.entrySet().stream()
                .map(entry -> {
                    int itemId = entry.getKey();
                    ItemStatCounter counter = entry.getValue();
                    String itemName = itemMap.getOrDefault(itemId, "아이템명 오류: " + itemId);

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
