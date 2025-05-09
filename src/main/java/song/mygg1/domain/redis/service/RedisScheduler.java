package song.mygg1.domain.redis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import song.mygg1.domain.riot.dto.champion.ChampionRotationsDto;
import song.mygg1.domain.riot.dto.league.LeagueListDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisScheduler {
    private final RedisService redisService;

    @Scheduled(cron = "0 0 12 * * TUE", zone = "Asia/Seoul")
    public void setChampionRotation() {
        log.info("챔피언 로테이션 캐시 갱신");
        ChampionRotationsDto championRotations = redisService.setChampionRotations();

        log.info("[free champion rotations] {}", championRotations.getFreeChampionIds());
        log.info("[free champion rotations(for N)] {}", championRotations.getFreeChampionIdsForNewPlayers());
    }

    @Scheduled(cron = "0 0 06 * * *", zone = "Asia/Seoul")
    public void setChallengerLeague() {
        log.info("챌린저 리그 랭킹 갱싱");
        LeagueListDto leagueList = redisService.setChallengerLeague();

        log.info("[챌린저 리그] {}", leagueList);
    }
}
