package song.mygg1.domain.riot.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import song.mygg1.domain.riot.dto.match.participant.ChampionMatchDto;
import song.mygg1.domain.riot.dto.match.MatchDto;
import song.mygg1.domain.riot.service.match.MatchService;

import java.util.List;

@Slf4j
@SpringBootTest
@TestPropertySource(properties = {
        "spring.profiles.active=test",
        "JASYPT_ENCRYPTOR_PASSWORD=test"
})
class MatchesServiceTest {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MatchService matchService;

    @Rollback(value = false)
    @Test
    void getMatches() throws JsonProcessingException {
        List<MatchDto> matchesList = matchService.getMatchList("RcyPGGrO09p4HXbXvOD8kG41bjIAfkLvlHQVy3XzFHk7avTsxtsmYqGnng40x3yQ-ph4Zb3qOax8CA", 0, 10);
        String res = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(matchesList);
        log.info(res);
    }

    @Test
    void getChampionMatches() throws JsonProcessingException {
        List<ChampionMatchDto> matchList = matchService.getChampionMatchList(110L, PageRequest.of(0, 10));
        String res = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(matchList);
        log.info(res);
    }

}