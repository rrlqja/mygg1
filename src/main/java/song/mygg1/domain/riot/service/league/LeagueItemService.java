package song.mygg1.domain.riot.service.league;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import song.mygg1.domain.redis.service.CacheService;
import song.mygg1.domain.riot.dto.account.AccountDto;
import song.mygg1.domain.riot.dto.champion.ChampionMasteryDto;
import song.mygg1.domain.riot.dto.league.LeagueItemDto;
import song.mygg1.domain.riot.dto.league.LeagueItemSummonerDto;
import song.mygg1.domain.riot.dto.summoner.SummonerDto;
import song.mygg1.domain.riot.entity.league.LeagueItem;
import song.mygg1.domain.riot.mapper.league.LeagueItemMapper;
import song.mygg1.domain.riot.repository.league.LeagueItemJpaRepository;
import song.mygg1.domain.riot.service.account.AccountService;
import song.mygg1.domain.riot.service.champion.ChampionMasteryService;
import song.mygg1.domain.riot.service.summoner.SummonerService;

import java.time.Duration;
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

    private static final Duration TTL = Duration.ofHours(12);
    private final LeagueItemMapper leagueItemMapper;

    @Transactional(readOnly = true)
    public Page<LeagueItemSummonerDto> getRankLeagueItemList(String leagueId, Pageable pageable) {
        if (leagueId == null || leagueId.isBlank()) {
            log.warn("leagueId cannot be null or blank getTopRankedLeagueItems");
            return Page.empty(pageable);
        }

        Page<LeagueItem> topLeagueItemEntities = leagueItemRepository.findLeagueItemsByLeagueId(leagueId, pageable);

        if (topLeagueItemEntities.isEmpty()) {
            log.info("No league items found in repository leagueId: {} with limit: {}", leagueId, pageable.getPageSize());
            return Page.empty(pageable);
        }

        List<LeagueItemSummonerDto> resultList = topLeagueItemEntities.getContent().stream()
                .map(this::buildDto)
                .flatMap(Optional::stream)
                .toList();

//        resultList.sort(Comparator.comparingInt(LeagueItemSummonerDto::getLeaguePoints).reversed());

        return new PageImpl<>(resultList, topLeagueItemEntities.getPageable(), topLeagueItemEntities.getTotalElements());
    }

    private Optional<LeagueItemSummonerDto> buildDto(LeagueItem itemEntity) {
        String puuid = itemEntity.getPuuid();
        if (puuid == null) {
            log.warn("PUUID is null in LeagueItem entity (summonerId: {})", itemEntity.getSummonerId());
            return Optional.empty();
        }

        String cacheKey = "league:item:summoner:" + puuid;

        try {
            LeagueItemSummonerDto dto = cacheService.getOrLoad(
                    cacheKey,
                    () -> buildResDto(itemEntity),
                    TTL
            );
            return Optional.of(dto);
        } catch (Exception ex) {
            log.warn("Failed to build LeagueItemSummonerDto for puuid={}, summonerId={}, Error: {}",
                    puuid, itemEntity.getSummonerId(), ex.getMessage(), ex);
            return Optional.empty();
        }
    }

    private LeagueItemSummonerDto buildResDto(LeagueItem itemEntity) {
        String puuid = itemEntity.getPuuid();
        if (puuid == null) {
            throw new IllegalArgumentException("PUUID cannot be null in LeagueItem entity (ID: " + itemEntity.getSummonerId());
        }

        log.debug("Building DTO for puuid: {}, summonerId: {}", puuid, itemEntity.getSummonerId());

        SummonerDto summoner = summonerService.getSummoner(puuid);
        AccountDto account = accountService.findAccountByPuuid(puuid);
        List<ChampionMasteryDto> championMastery = championMasteryService.getChampionMastery(puuid, 3);

        LeagueItemDto itemDto = leagueItemMapper.toDto(itemEntity);

        return new LeagueItemSummonerDto(itemDto, summoner, account, championMastery);
    }
}
