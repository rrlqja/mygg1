package song.mygg1.domain.riot.service.champion;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import song.mygg1.domain.common.exception.riot.champion.exceptons.ChampionNotFoundException;
import song.mygg1.domain.common.exception.riot.riotapi.RiotApiException;
import song.mygg1.domain.riot.service.datadragon.DataDragonService;
import song.mygg1.domain.redis.service.CacheService;
import song.mygg1.domain.riot.dto.account.AccountDto;
import song.mygg1.domain.riot.dto.champion.ChampionDto;
import song.mygg1.domain.riot.dto.champion.ChampionInfoDto;
import song.mygg1.domain.riot.dto.champion.ChampionMasteryRankingDto;
import song.mygg1.domain.riot.dto.league.LeagueItemDto;
import song.mygg1.domain.riot.dto.league.LeagueListDto;
import song.mygg1.domain.riot.dto.match.info.ChampionUsageDto;
import song.mygg1.domain.riot.dto.match.participant.ChampionWinRatePerDateDto;
import song.mygg1.domain.riot.dto.summoner.SummonerDto;
import song.mygg1.domain.riot.entity.champion.Champion;
import song.mygg1.domain.riot.repository.champion.ChampionJpaRepository;
import song.mygg1.domain.riot.repository.champion.ChampionMasterJpaRepository;
import song.mygg1.domain.riot.service.ApiService;
import song.mygg1.domain.riot.service.account.AccountService;
import song.mygg1.domain.riot.service.league.LeagueListService;
import song.mygg1.domain.riot.service.match.InfoService;
import song.mygg1.domain.riot.service.match.ParticipantService;
import song.mygg1.domain.riot.service.summoner.SummonerService;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChampionService {
    private final CacheService<ChampionDto> cacheService;
    private final CacheService<List<ChampionMasteryRankingDto>> championMasteryRankingCacheService;
    private final ChampionJpaRepository championRepository;
    private final ChampionMasterJpaRepository championMasterRepository;
    private final DataDragonService dataDragonService;
    private final ParticipantService participantService;
    private final InfoService infoService;
    private final LeagueListService leagueListService;
    private final AccountService accountService;
    private final SummonerService summonerService;
    private final ApiService apiService;
    private final Environment env;

    private static final ZoneId kst = ZoneId.of("Asia/Seoul");
    private static final Duration CHAMPION_TTL = Duration.ofDays(1);
    private static final Duration CHAMPION_MASTERY_TTL = Duration.ofDays(1);

    @PostConstruct
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void setChampionList() {
        JsonNode root = apiService.getChampionJson(dataDragonService.getVersion())
                .orElseThrow(() -> new RiotApiException("Failed to fetch champions"));

        JsonNode dataNode = root.path("data");
        List<Champion> championList = new ArrayList<>();

        Iterator<Map.Entry<String, JsonNode>> fields = dataNode.fields();
        while (fields.hasNext()) {
            JsonNode champNode = fields.next().getValue();
            Champion champion = Champion.create(
                    champNode.get("key").asLong(),
                    champNode.get("id").asText(),
                    champNode.get("name").asText(),
                    champNode.get("title").asText(),
                    champNode.get("blurb").asText()
            );
            championList.add(champion);
        }

        List<Champion> saved = championRepository.saveAll(championList);
        saved.forEach(champ -> {
            ChampionDto dto = new ChampionDto(champ);
            String key = "champion:key:" + champ.getKey();
            cacheService.put(key, dto, CHAMPION_TTL);
        });
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<ChampionDto> getChampion(List<Long> championIdList) {
        return championIdList.stream()
                .map(id -> {
                    String key = "champion:key:" + id;
                    return getChampionDto(id, key);
                })
                .toList();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String getChampionId(Long championKey) {
        String key = "champion:key:" + championKey;

        return cacheService.getOrLoad(
                key,
                () -> new ChampionDto(
                        championRepository.findChampionByKey(championKey)
                                .orElseThrow(ChampionNotFoundException::new)),
                CHAMPION_TTL
        ).getId();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ChampionInfoDto getChampionInfo(Long id) {
        String key = "champion:key:" + id;
        ChampionDto champion = getChampionDto(id, key);

        List<ChampionWinRatePerDateDto> championWinRatePerDate = participantService.getChampionWinRatePerDate(champion.getId());
        List<ChampionUsageDto> championUsage = infoService.getChampionUsage(champion.getKey());

        return new ChampionInfoDto(champion, championWinRatePerDate, championUsage);
    }

    private ChampionDto getChampionDto(Long id, String key) {
        return cacheService.getOrLoad(
                key,
                () -> championRepository.findChampionByKey(id)
                        .map(ChampionDto::new)
                        .orElseThrow(ChampionNotFoundException::new),
                CHAMPION_TTL);
    }

    @PostConstruct
    @Transactional
    public void initDev() {
        if (env.acceptsProfiles(Profiles.of("dev"))) {
            setChampionMasteryRanking();
        }
    }

    @Transactional
    @Scheduled(cron = "0 0 22 * * ?")
    public void setChampionMasteryRanking() {
        List<Champion> championList = championRepository.findAll();

        LocalDate firstDayOfYear = LocalDate.now(ZoneId.of("Asia/Seoul")).withDayOfYear(1);
        LocalDate lastDayPrev  = firstDayOfYear.withDayOfMonth(firstDayOfYear.lengthOfMonth());

        Long startDate = firstDayOfYear
                .atStartOfDay(kst)
                .toInstant()
                .toEpochMilli();

        Long endDate = lastDayPrev
                .atTime(LocalTime.MAX)
                .atZone(kst)
                .toInstant()
                .toEpochMilli();

        LeagueListDto challengerLeague = leagueListService.getChallengerLeagueList();
        if (challengerLeague == null || challengerLeague.getEntries() == null || challengerLeague.getEntries().isEmpty()) {
            log.warn("Challenger league list is empty. Skipping champion mastery ranking update.");
            return;
        }
        List<String> challengerPuuids = challengerLeague.getEntries().stream()
                .map(LeagueItemDto::getPuuid)
                .toList();

        for (Champion champion : championList) {
            Long championKey = champion.getKey();
            log.info("Setting champion mastery ranking for champion key: {}", championKey);

            try {
                String masteryCacheKey = String.format(
                        "champion:mastery:ranking:%d:%d_%d",
                        championKey, startDate, endDate
                );

                List<ChampionMasteryRankingDto> rankingData =
                        calculateChampionMasteryRankingInternal(championKey, challengerPuuids, startDate, endDate);

                if (rankingData != null && !rankingData.isEmpty()) {
                    championMasteryRankingCacheService.put(masteryCacheKey, rankingData, CHAMPION_MASTERY_TTL);
//                    log.info("Successfully calculated and cached mastery ranking for champion key: {}. Entries: {}", championKey, rankingData.size());
                } else {
//                    log.warn("No mastery ranking data found or calculated for champion key: {}. Cache not updated.", championKey);
                    championMasteryRankingCacheService.evict(masteryCacheKey);
                }
            } catch (Exception e) {
                log.error("Error setting champion mastery ranking for champion key: {}. Error: {}", championKey, e.getMessage(), e);
            }
        }
        log.info("Finished scheduled job for setting all champion mastery rankings.");
    }

    private List<ChampionMasteryRankingDto> calculateChampionMasteryRankingInternal(Long championKey,
                                                                                    List<String> puuidList,
                                                                                    Long startDate,
                                                                                    Long endDate) {
        List<ChampionMasteryRankingDto> rankingWithoutAccountInfo =
                championMasterRepository.getChampionMasteryRanking(championKey, puuidList, startDate, endDate);

        return rankingWithoutAccountInfo.stream()
                .map(dto -> {
                    try {
                        AccountDto account = accountService.findAccountByPuuidAndUpdate(dto.getPuuid());
                        dto.setAccount(account);

                        SummonerDto summoner = summonerService.getSummoner(dto.getPuuid());
                        dto.setSummoner(summoner);
                    } catch (Exception e) {
                        log.warn("Failed to fetch account/summoner info for puuid {} in mastery ranking for championKey {}: {}",
                                dto.getPuuid(), championKey, e.getMessage());
                    }
                    return dto;
                })
                .filter(dto -> dto.getAccount() != null && dto.getSummoner() != null)
                .toList();
    }

    @Transactional
    public List<ChampionMasteryRankingDto> getChampionMasteryRanking(Long championId) {
        LeagueListDto challengerLeague = leagueListService.getChallengerLeagueList();

        String key = "champion:key:" + championId;
        ChampionDto champion = getChampionDto(championId, key);

        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        LocalDate firstDayOfYear = today.withDayOfYear(1);
        LocalDate lastDayPrev  = firstDayOfYear.withDayOfMonth(firstDayOfYear.lengthOfMonth());

        Long startDate = firstDayOfYear
                .atStartOfDay(kst)
                .toInstant()
                .toEpochMilli();

        Long endDate = lastDayPrev
                .atTime(LocalTime.MAX)
                .atZone(kst)
                .toInstant()
                .toEpochMilli();

        String masteryKey = String.format(
                "champion:mastery:ranking:%d:%d_%d",
                championId, startDate, endDate
        );

        return championMasteryRankingCacheService.getOrLoad(
                masteryKey,
                () -> championMasterRepository.getChampionMasteryRanking(champion.getKey(), challengerLeague.getEntries().stream().map(LeagueItemDto::getPuuid).toList(), startDate, endDate).stream()
                        .map(dto -> {
                            AccountDto account = accountService.findAccountByPuuid(dto.getPuuid());
                            dto.setAccount(account);
                            SummonerDto summoner = summonerService.getSummoner(dto.getPuuid());
                            dto.setSummoner(summoner);
                            return dto;
                        })
                        .toList(),
                CHAMPION_MASTERY_TTL
        );
    }
}
