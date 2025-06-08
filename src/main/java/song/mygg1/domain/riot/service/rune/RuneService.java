package song.mygg1.domain.riot.service.rune;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import song.mygg1.domain.common.exception.riot.riotapi.RiotApiException;
import song.mygg1.domain.redis.service.CacheService;
import song.mygg1.domain.riot.dto.rune.RuneResponseDto;
import song.mygg1.domain.riot.dto.rune.RuneStyleDto;
import song.mygg1.domain.riot.entity.rune.RuneStyle;
import song.mygg1.domain.riot.mapper.rune.RuneStyleMapper;
import song.mygg1.domain.riot.repository.rune.RuneStyleJpaRepository;
import song.mygg1.domain.riot.service.ApiService;
import song.mygg1.domain.riot.service.datadragon.DataDragonService;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RuneService {
    private final CacheService<RuneStyleDto> cacheService;
    private final DataDragonService dataDragonService;
    private final RuneStyleJpaRepository runeStyleRepository;
    private final ApiService apiService;
    private final ObjectMapper objectMapper;
    private final RuneStyleMapper runeStyleMapper;

    private static final Duration RUNE_TTL = Duration.ofDays(1);

    @PostConstruct
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void setRuneStyles() throws IOException {
        JsonNode root = apiService.getRuneJson(dataDragonService.getVersion())
                .orElseThrow(() -> new RiotApiException("Failed to fetch items"));

        List<RuneStyle> runeStyleList = new ArrayList<>();

        JavaType type = objectMapper.getTypeFactory()
                .constructCollectionType(List.class, RuneStyleDto.class);
        List<RuneStyleDto> res = objectMapper.readValue(
                root.traverse(),
                type
        );
        res.forEach((runeStyle) -> {
            RuneStyle entity = runeStyleMapper.toEntity(runeStyle);
            log.info("Add rune style: {}", runeStyle);
            runeStyleList.add(entity);
        });

        List<RuneStyle> saved = runeStyleRepository.saveAll(runeStyleList);

        saved.forEach(runeStyle -> {
            RuneStyleDto dto = runeStyleMapper.toDto(runeStyle);
            String id = "rune:id:" + runeStyle.getId();
            cacheService.put(id, dto, RUNE_TTL);
            log.info("dto: {}", dto);
        });
    }
}
