package song.mygg1.domain.riot.service.champion;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import song.mygg1.domain.redis.service.CacheService;
import song.mygg1.domain.riot.dto.champion.ChampionLevelSkillStatsResponse;
import song.mygg1.domain.riot.dto.champion.ChampionRuneStatsResponse;
import song.mygg1.domain.riot.dto.champion.LevelSkillData;
import song.mygg1.domain.riot.dto.champion.RuneCountDto;
import song.mygg1.domain.riot.dto.champion.RuneStatDto;
import song.mygg1.domain.riot.dto.match.championbuild.AggregatedCoreItemStatsDto;
import song.mygg1.domain.riot.dto.match.championbuild.CoreItemStatDto;
import song.mygg1.domain.riot.dto.match.championbuild.ItemStatCounter;
import song.mygg1.domain.riot.dto.match.championbuild.MatchPlayerInfo;
import song.mygg1.domain.riot.entity.champion.Champion;
import song.mygg1.domain.riot.entity.item.Item;
import song.mygg1.domain.riot.entity.match.participant.PerkStyle;
import song.mygg1.domain.riot.entity.timeline.events.ItemPurchaseEvent;
import song.mygg1.domain.riot.entity.timeline.events.SkillLevelUpEvent;
import song.mygg1.domain.riot.repository.champion.ChampionJpaRepository;
import song.mygg1.domain.riot.repository.item.ItemJpaRepository;
import song.mygg1.domain.riot.repository.match.MatchJpaRepository;
import song.mygg1.domain.riot.repository.match.ParticipantJpaRepository;
import song.mygg1.domain.riot.repository.timeline.ItemPurchaseEventJpaRepository;
import song.mygg1.domain.riot.repository.timeline.SkillLevelUpEventJpaRepository;

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
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChampionBuildService {
    private final CacheService<AggregatedCoreItemStatsDto> itemBuildCacheService;
    private final CacheService<ChampionLevelSkillStatsResponse> skillTreeCacheService;
    private final CacheService<ChampionRuneStatsResponse> runeCacheService;
    private final MatchJpaRepository matchRepository;
    private final ItemPurchaseEventJpaRepository itemPurchaseEventRepository;
    private final SkillLevelUpEventJpaRepository sKillLevelUpEventRepository;
    private final ChampionJpaRepository championRepository;
    private final ItemJpaRepository itemRepository;
    private final ParticipantJpaRepository participantRepository;
    private final Environment env;

    private static final ZoneId kst = ZoneId.of("Asia/Seoul");
    private static final Duration ITEM_BUILD_TTL = Duration.ofDays(1);
    private static final Duration SKILL_TREE_TTL = Duration.ofDays(1);
    private static final Duration RUNE_TTL = Duration.ofDays(1);
    private static final int FIRST_CORE_LIMIT = 3;
    private static final int SECOND_CORE_LIMIT = 3;
    private static final int THIRD_CORE_LIMIT = 5;

    @Transactional
    @PostConstruct
    public void initDev() {
        if (env.acceptsProfiles(Profiles.of("dev"))) {
            setChampionBuild();
        }
    }

    @Transactional
    @Scheduled(cron = "0 0 22 * * ?")
    public void setChampionBuild() {
        List<Champion> championList = championRepository.findAll();

        for (Champion champion : championList) {
            log.info("set champion build champion {}:{}", champion.getId(), champion.getKey());

            try {
                log.info("set champion skill build champion {}:{}", champion.getId(), champion.getKey());
                String skillTreeKey = "champion:build:skill:" + champion.getKey().intValue();
                ChampionLevelSkillStatsResponse skillTree = setChampionSkillTree(champion.getKey().intValue());
                skillTreeCacheService.put(skillTreeKey, skillTree, SKILL_TREE_TTL);

                log.info("set champion item build champion {}:{}", champion.getId(), champion.getKey());
                String itemBuildKey = "champion:build:item:" + champion.getKey().intValue();
                AggregatedCoreItemStatsDto itemBuild = setChampionItemBuild(champion.getKey().intValue());
                itemBuildCacheService.put(itemBuildKey, itemBuild, ITEM_BUILD_TTL);

                log.info("set champion rune build champion {}:{}", champion.getId(), champion.getKey());
                String runeKey = "champion:build:rune:" + champion.getKey().intValue();
                ChampionRuneStatsResponse rune = setChampionRune(champion.getKey().intValue());
                runeCacheService.put(runeKey, rune, RUNE_TTL);
            } catch (Exception e) {
                log.error("error champion build champion {}:{}", champion.getId(), champion.getKey(), e);
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ChampionRuneStatsResponse getChampionRune(Integer championId) {
        String key = "champion:build:rune:" + championId;

        log.info("get champion rune key {}", key);
        return runeCacheService.getOrLoad(
                key,
                () -> setChampionRune(championId),
                SKILL_TREE_TTL
        );
    }

    @Transactional
    public ChampionRuneStatsResponse setChampionRune(Integer championId) {
        ZonedDateTime endZonedDateTime = ZonedDateTime.now(kst);
        Instant endInstant = endZonedDateTime.toInstant();
        long endTimestamp = endInstant.toEpochMilli();

        LocalDate startDateValue = LocalDate.now(kst).minusMonths(3).withDayOfMonth(1);
        ZonedDateTime startZonedDateTime = startDateValue.atStartOfDay(kst);
        Instant startInstant = startZonedDateTime.toInstant();
        long startTimestamp = startInstant.toEpochMilli();

        long analyzedGames = participantRepository.countTotalGames(championId, startTimestamp, endTimestamp);
        log.info("Total analyzed games for champion {}: {}", championId, analyzedGames);

        if (analyzedGames == 0) {
            log.warn("No rune data found for championId: {}", championId);
            return new ChampionRuneStatsResponse(championId, 0, null, null, Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
        }

        RuneStatDto mostPrimaryStyle = findMostPicked(
                participantRepository.findPrimaryStylePicks(championId, startTimestamp, endTimestamp), analyzedGames);

        RuneStatDto mostSecondaryStyle = findMostPicked(
                participantRepository.findSecondaryStylePicks(championId, startTimestamp, endTimestamp), analyzedGames);

        List<PerkStyle> perkStyles = participantRepository.findPerkStylesWithSelections(championId, startTimestamp, endTimestamp);
        Map<String, List<RuneCountDto>> runePicksBySlot = processRunePicksBySlot(perkStyles);

        List<RuneStatDto> primaryRunes = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            primaryRunes.add(findMostPicked(runePicksBySlot.getOrDefault("primary_" + i, Collections.emptyList()), analyzedGames));
        }

        List<RuneStatDto> secondaryRunes = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            secondaryRunes.add(findMostPicked(runePicksBySlot.getOrDefault("sub_" + i, Collections.emptyList()), analyzedGames));
        }

        List<RuneStatDto> statPerkRunes = new ArrayList<>();
        statPerkRunes.add(findMostPicked(
                participantRepository.findStatPerkOffensePicks(championId, startTimestamp, endTimestamp), analyzedGames));
        statPerkRunes.add(findMostPicked(
                participantRepository.findStatPerkFlexPicks(championId, startTimestamp, endTimestamp), analyzedGames));
        statPerkRunes.add(findMostPicked(
                participantRepository.findStatPerkDefensePicks(championId, startTimestamp, endTimestamp), analyzedGames));

        return new ChampionRuneStatsResponse(championId, (int) analyzedGames, mostPrimaryStyle, mostSecondaryStyle, primaryRunes, secondaryRunes, statPerkRunes);
    }

    private Map<String, List<RuneCountDto>> processRunePicksBySlot(List<PerkStyle> perkStyles) {
        Map<String, List<RuneCountDto>> result = new HashMap<>();

        for (int i = 0; i < 4; i++) {
            final int slotIndex = i;
            Map<Integer, Long> counts = perkStyles.stream()
                    .filter(ps -> "primaryStyle".equals(ps.getDescription()) && ps.getSelections().size() > slotIndex)
                    .map(ps -> ps.getSelections().get(slotIndex).getPerk())
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
            result.put("primary_" + i, convertMapToDtoList(counts));
        }

        for (int i = 0; i < 2; i++) {
            final int slotIndex = i;
            Map<Integer, Long> counts = perkStyles.stream()
                    .filter(ps -> "subStyle".equals(ps.getDescription()) && ps.getSelections().size() > slotIndex)
                    .map(ps -> ps.getSelections().get(slotIndex).getPerk())
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
            result.put("sub_" + i, convertMapToDtoList(counts));
        }

        return result;
    }

    private List<RuneCountDto> convertMapToDtoList(Map<Integer, Long> map) {
        return map.entrySet().stream()
                .map(entry -> new RuneCountDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private RuneStatDto findMostPicked(List<RuneCountDto> pickCounts, long totalGames) {
        if (pickCounts.isEmpty() || totalGames == 0) {
            return new RuneStatDto(0, 0.0, 0);
        }

        RuneCountDto mostPicked = pickCounts.stream()
                .max(Comparator.comparing(RuneCountDto::getPickCount))
                .orElse(new RuneCountDto(0, 0L));

        int runeId = mostPicked.getId();
        long pickCount = mostPicked.getPickCount();
        double pickRate = (totalGames > 0)
                ? Math.round(((double) pickCount / totalGames) * 10000.0) / 100.0
                : 0.0;

        return new RuneStatDto(runeId, pickRate, (int) pickCount);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ChampionLevelSkillStatsResponse getChampionSkillTree(Integer championId) {
        String key = "champion:build:skill:" + championId;

        return skillTreeCacheService.getOrLoad(
                key,
                () -> setChampionSkillTree(championId),
                SKILL_TREE_TTL
        );
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ChampionLevelSkillStatsResponse setChampionSkillTree(Integer championId) {
        ZonedDateTime endZonedDateTime = ZonedDateTime.now(kst);
        Instant endInstant = endZonedDateTime.toInstant();
        long endTimestamp = endInstant.toEpochMilli();

        LocalDate startDateValue = LocalDate.now(kst).minusMonths(3).withDayOfMonth(1);
        ZonedDateTime startZonedDateTime = startDateValue.atStartOfDay(kst);
        Instant startInstant = startZonedDateTime.toInstant();
        long startTimestamp = startInstant.toEpochMilli();

        List<MatchPlayerInfo> targetPlayers = matchRepository.findMatchPlayerInfoByChampionAndPeriod(championId, startTimestamp, endTimestamp, PageRequest.of(0, 800));
//        log.info("Found {} matches", targetPlayers.size());

        Map<Integer, Map<Integer, Integer>> skillTreeFrequency = new HashMap<>();
        for (int level = 1; level <= 18; level++) {
            Map<Integer, Integer> skillCountsForLevel = new HashMap<>();

            for(int slot = 1; slot <= 4; slot++) skillCountsForLevel.put(slot, 0);
            skillTreeFrequency.put(level, skillCountsForLevel);
        }
        int analyzedMatchCount = 0;

        for (MatchPlayerInfo matchInfo : targetPlayers) {
//            log.info("Analyzing matchId: {}, participantId: {}", matchInfo.getMatchId(), matchInfo.getParticipantId());
            List<SkillLevelUpEvent> skillEvents = sKillLevelUpEventRepository.findByMatchIdsInAndLevelUpType(
                    matchInfo.getMatchId(),
                    matchInfo.getParticipantId()
            );
//            log.info("matchId: {}, participantId: {}, found {} skill events.",
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
                log.debug("Skill sequence matchId {}: {}", matchInfo.getMatchId(), analyzedMatchCount);
            } else {
                log.debug("No SKILL_LEVEL_UP events found matchId: {} participantId: {}", matchInfo.getMatchId(), matchInfo.getParticipantId());
            }
        }

        if (analyzedMatchCount == 0) {
            log.info("No skill data found championId: {}", championId);
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
                    percentages.put(skillSlotToString(slot), Math.floor((double) count * 100.0 / totalPicksAtThisLevelOrder * 100) / 100.0);
                    if (count > maxPicks) {
                        maxPicks = count;
                        mostPicked = skillSlotToString(slot);
                    } else if (count == maxPicks && maxPicks != 0) {
                        mostPicked += "/" + skillSlotToString(slot);
                    }
                }
            } else {
                for (int slot = 1; slot <= 4; slot++) percentages.put(skillSlotToString(slot), Math.floor(0.0 * 100) / 100.0);
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

        return itemBuildCacheService.getOrLoad(
                key,
                () -> setChampionItemBuild(championId),
                ITEM_BUILD_TTL
        );
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
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

        List<MatchPlayerInfo> infoList = matchRepository.findMatchPlayerInfoByChampionAndPeriod(championId, start, end, PageRequest.of(0, 500));
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
