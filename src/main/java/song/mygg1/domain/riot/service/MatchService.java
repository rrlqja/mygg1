package song.mygg1.domain.riot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import song.mygg1.domain.common.exception.riot.match.exceptions.MatchNotFoundException;
import song.mygg1.domain.riot.dto.match.MatchDto;
import song.mygg1.domain.riot.entity.match.Matches;
import song.mygg1.domain.riot.repository.MatchJpaRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchJpaRepository matchRepository;
    private final ApiService apiService;

    @Transactional
    public List<Matches> getMatchList(String puuid, Integer start, Integer count) {
        List<String> matchIdList = apiService.getMatches(puuid, start, count);

        List<Matches> matchesList = matchRepository.findMatchesByMatchIdIn(matchIdList);

        Set<String> existingMatchIdList = matchesList.stream()
                .map(Matches::getMatchId)
                .collect(Collectors.toSet());

        List<String> missingMatchIdList = matchIdList.stream()
                .filter(matchId -> !existingMatchIdList.contains(matchId))
                .toList();

        List<Matches> missingMatchesList = missingMatchIdList.stream()
                .map(apiService::getMatchDetail)
                .flatMap(Optional::stream)
                .map(MatchDto::toEntity)
                .toList();

        matchRepository.saveAll(missingMatchesList);

        Map<String, Matches> matchMap = Stream.concat(matchesList.stream(), missingMatchesList.stream())
                .collect(Collectors.toMap(Matches::getMatchId, Function.identity()));

        return matchIdList.stream()
                .map(matchMap::get)
                .toList();
    }

    @Transactional
    public MatchDto getMatchDetail(String matchId, String puuid) {
        Matches match = matchRepository.findMatchesByMatchId(matchId)
                .orElseGet(() -> {
                    MatchDto matchDto = apiService.getMatchDetail(matchId)
                            .orElseThrow(MatchNotFoundException::new);

                    return matchRepository.save(matchDto.toEntity());
                });

        return new MatchDto(match, puuid);
    }

    @Transactional
    public List<MatchDto> getMoreMatchList(String puuid, Integer start, Integer count) {
        List<Matches> matchList = getMatchList(puuid, start, count);

        return matchList.stream()
                .map(m->new MatchDto(m, puuid))
                .toList();
    }
}
