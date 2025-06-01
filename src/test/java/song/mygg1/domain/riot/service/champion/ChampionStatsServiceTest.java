package song.mygg1.domain.riot.service.champion;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import song.mygg1.domain.riot.dto.champion.ChampionBanPickDto;

import java.util.List;

@Slf4j
@SpringBootTest
@TestPropertySource(properties = {
        "spring.profiles.active=test",
        "JASYPT_ENCRYPTOR_PASSWORD=test"
})
class ChampionStatsServiceTest {
    @Autowired
    ChampionStatsService championStatsService;

    @Test
    void getChampionBanPick() {
        ChampionBanPickDto championStats = championStatsService.getChampionStats(235L);
        log.info(championStats.toString());
    }

    @Test
    void getAllChampionBanPick() {
        List<ChampionBanPickDto> banPickList = championStatsService.getAllChampionStats();

        for (ChampionBanPickDto championBanPickDto : banPickList) {
            log.info(championBanPickDto.toString());
        }
    }

}