package song.mygg1.domain.riot.service.match;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import song.mygg1.domain.common.exception.riot.match.exceptions.MatchNotFoundException;
import song.mygg1.domain.redis.service.CacheService;
import song.mygg1.domain.redis.service.match.MatchCacheLimiterService;
import song.mygg1.domain.riot.dto.league.LeagueItemDto;
import song.mygg1.domain.riot.dto.league.LeagueListDto;
import song.mygg1.domain.riot.dto.match.participant.ChampionMatchDto;
import song.mygg1.domain.riot.dto.match.MatchDto;
import song.mygg1.domain.riot.entity.match.Matches;
import song.mygg1.domain.riot.mapper.match.MatchMapper;
import song.mygg1.domain.riot.repository.match.MatchJpaRepository;
import song.mygg1.domain.riot.service.ApiService;
import song.mygg1.domain.riot.service.league.LeagueService;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchService {
    private final CacheService<MatchDto> cacheService;
    private final MatchCacheLimiterService cacheLimiterService;
    private final LeagueService leagueService;
    private final MatchJpaRepository matchRepository;
    private final ApiService apiService;
    private final MatchMapper matchMapper;

    private static final Duration MATCH_DETAIL_TTL = Duration.ofMinutes(10);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public MatchDto getMatchDetail(String matchId, String puuid) {
        String key = "match:detail:" + matchId;

        MatchDto dto = cacheService.getOrLoad(
                key,
                () -> matchMapper.toDto(getOrFetchMatch(matchId), puuid),
                MATCH_DETAIL_TTL
        );

//        cacheLimiterService.trackAndTrim(key);

        return dto;
    }

    private Matches getOrFetchMatch(String matchId) {
        return matchRepository.findMatchesByMatchId(matchId)
                .orElseGet(() -> {
                    MatchDto dto = apiService.getMatchDetail(matchId)
                            .orElseThrow(MatchNotFoundException::new);
                    Matches entity = matchMapper.toEntity(dto);
                    return matchRepository.save(entity);
                });
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<MatchDto> getMatchList(String puuid, int start, int count) {
        List<String> matchIds = apiService.getMatches(puuid, start, count);
        return matchIds.stream()
                .map(id -> {
                    try {
                        return getMatchDetail(id, puuid);
                    } catch (Exception e) {
                        log.warn("매치 상세 조회 실패 (id={}): {}", id, e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<MatchDto> getMoreMatchList(String puuid, int start, int count) {
        return getMatchList(puuid, start, count);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<MatchDto> refreshMatchList(String puuid, int start, int count) {
        List<String> matchIds = apiService.getMatches(puuid, start, count);

        return matchIds.stream()
                .map(id -> {
                    log.info("refreshMatchList: {}", id);
                    cacheService.evict("match:detail:" + id);
                    return getMatchDetail(id, puuid);
                })
                .toList();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<ChampionMatchDto> getChampionMatchList(Long championId, Pageable pageable) {
        Page<Matches> matchList = matchRepository.findMatchesByChampion(championId, pageable);

        return matchList.stream()
                .map(m -> getMatchDetail(m.getMatchId(), null))
                .map(mdto-> matchMapper.toChampionMatchDto(mdto, championId.intValue()))
                .toList();
    }

    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void getChallengerLeagueMatches() {
        LeagueListDto challengerLeague = leagueService.getChallengerLeague();

        challengerLeague.getEntries().stream()
                .map(LeagueItemDto::getPuuid)
                .forEach(puuid -> {
                    try {
                        List<MatchDto> matches = refreshMatchList(puuid, 0, 10);
                        log.info("[MatchService] update challenger match, puuid = {},", puuid);
                    } catch (Exception e) {
                        log.error("[MatchService] update failed challenger match, puuid = {} 매치 조회/저장 오류: {}", puuid, e.getMessage(), e);
                    }
                });
    }
}
