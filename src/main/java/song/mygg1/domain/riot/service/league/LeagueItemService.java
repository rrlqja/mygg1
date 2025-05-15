package song.mygg1.domain.riot.service.league;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import song.mygg1.domain.common.exception.riot.league.exceptions.LeagueItemNotFoundException;
import song.mygg1.domain.redis.service.CacheService;
import song.mygg1.domain.riot.dto.account.AccountDto;
import song.mygg1.domain.riot.dto.champion.ChampionMasteryDto;
import song.mygg1.domain.riot.dto.league.LeagueItemDto;
import song.mygg1.domain.riot.dto.league.LeagueItemSummonerDto;
import song.mygg1.domain.riot.dto.league.LeagueListDto;
import song.mygg1.domain.riot.dto.summoner.SummonerDto;
import song.mygg1.domain.riot.entity.league.LeagueItem;
import song.mygg1.domain.riot.repository.league.LeagueItemJpaRepository;
import song.mygg1.domain.riot.service.account.AccountService;
import song.mygg1.domain.riot.service.champion.ChampionMasteryService;
import song.mygg1.domain.riot.service.summoner.SummonerService;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LeagueItemService {
    private final CacheService<LeagueItemSummonerDto> cacheService;
    private final LeagueItemJpaRepository leagueItemRepository;
    private final SummonerService summonerService;
    private final AccountService accountService;
    private final ChampionMasteryService championMasteryService;

    private static final Duration TTL = Duration.ofMinutes(30);

    @Transactional
    public List<LeagueItemSummonerDto> getLeagueItemList(LeagueListDto leagueDto) {
        return getLeagueItemList(leagueDto, 10);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<LeagueItemSummonerDto> getLeagueItemList(LeagueListDto leagueDto, int limit) {
        List<String> ids = leagueDto.getEntries().stream()
                .map(LeagueItemDto::getSummonerId)
                .limit(limit)
                .toList();

        return ids.stream()
                .map(this::safeGetDto)
                .flatMap(Optional::stream)
                .sorted(Comparator.comparingInt(LeagueItemSummonerDto::getLeaguePoints).reversed())
                .limit(limit)
                .toList();
    }

    private Optional<LeagueItemSummonerDto> safeGetDto(String summonerId) {
        try {
            LeagueItemSummonerDto dto = cacheService.getOrLoad(
                    "league:item:" + summonerId,
                    () -> buildDto(summonerId),
                    TTL
            );
            return Optional.of(dto);
        } catch (Exception ex) {
            log.warn("Failed to build LeagueItemSummonerDto for summonerId={}, skipping. Error: {}",
                    summonerId, ex.getMessage());
            return Optional.empty();
        }
    }

    private LeagueItemSummonerDto buildDto(String summonerId) {
        LeagueItem item = leagueItemRepository.findLeagueItemBySummonerId(summonerId)
                .orElseThrow(LeagueItemNotFoundException::new);
        log.info("LeagueItem: {}", item);
        SummonerDto summoner = summonerService.getSummoner(item.getPuuid());
        AccountDto account = accountService.findAccountByPuuid(item.getPuuid());
        List<ChampionMasteryDto> championMastery = championMasteryService.getChampionMastery(account.getPuuid(), 3);

        return new LeagueItemSummonerDto(item, summoner, account, championMastery);
    }
}
