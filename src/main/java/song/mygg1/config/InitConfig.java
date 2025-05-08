package song.mygg1.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import song.mygg1.domain.common.service.DataDragonService;
import song.mygg1.domain.redis.service.RedisService;
import song.mygg1.domain.riot.service.ApiService;
import song.mygg1.domain.riot.service.champion.ChampionService;

@Slf4j
@Component
@Profile("!dev")
@RequiredArgsConstructor
public class InitConfig {
    private final InitService initService;

    @PostConstruct
    public void setInit() {
        initService.init();
    }

    @Component
    @RequiredArgsConstructor
    private static class InitService {
        private final RedisService redisService;
        private final DataDragonService dataDragonService;
        private final ChampionService championService;
        private final ApiService apiService;

        public void init() {
            championService.getChampionList();
        }
    }
}
