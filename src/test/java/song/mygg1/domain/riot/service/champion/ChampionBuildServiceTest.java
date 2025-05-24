package song.mygg1.domain.riot.service.champion;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import song.mygg1.domain.riot.dto.match.championbuild.AggregatedCoreItemStatsDto;
import song.mygg1.domain.riot.dto.match.championbuild.CoreItemStatDto;

import java.util.List;

@Slf4j
@SpringBootTest
@TestPropertySource(properties = {
        "spring.profiles.active=test",
        "JASYPT_ENCRYPTOR_PASSWORD=test"
})
class ChampionBuildServiceTest {
    @Autowired
    ChampionBuildService championBuildService;

    @Test
    void getChampionItemBuild() {
        AggregatedCoreItemStatsDto championBuild = championBuildService.getChampionItemBuild(80);

        List<CoreItemStatDto> firstCoreItemStats = championBuild.getFirstCoreItemStats();
        List<CoreItemStatDto> secondCoreItemStats = championBuild.getSecondCoreItemStats();
        List<CoreItemStatDto> thirdCoreItemStats = championBuild.getThirdCoreItemStats();
        log.info("firstCore: {}", firstCoreItemStats);
        log.info("secondCore: {}", secondCoreItemStats);
        log.info("thirdCore: {}", thirdCoreItemStats);
    }

}