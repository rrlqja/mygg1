package song.mygg1.domain.redis.config;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import song.mygg1.domain.redis.service.BaseCacheService;
import song.mygg1.domain.redis.service.CacheService;
import song.mygg1.domain.riot.dto.account.AccountDto;
import song.mygg1.domain.riot.dto.champion.ChampionBanPickDto;
import song.mygg1.domain.riot.dto.champion.ChampionDto;
import song.mygg1.domain.riot.dto.champion.ChampionLevelSkillStatsResponse;
import song.mygg1.domain.riot.dto.champion.ChampionMasteryDto;
import song.mygg1.domain.riot.dto.champion.ChampionMasteryRankingDto;
import song.mygg1.domain.riot.dto.champion.ChampionRotationsDto;
import song.mygg1.domain.riot.dto.champion.ChampionRuneStatsResponse;
import song.mygg1.domain.riot.dto.item.ItemDto;
import song.mygg1.domain.riot.dto.league.LeagueEntryDto;
import song.mygg1.domain.riot.dto.league.LeagueItemSummonerDto;
import song.mygg1.domain.riot.dto.league.LeagueListDto;
import song.mygg1.domain.riot.dto.match.championbuild.AggregatedCoreItemStatsDto;
import song.mygg1.domain.riot.dto.match.info.ChampionUsageDto;
import song.mygg1.domain.riot.dto.match.participant.ChampionWinRatePerDateDto;
import song.mygg1.domain.riot.dto.match.MatchDto;
import song.mygg1.domain.riot.dto.match.participant.WinRateDto;
import song.mygg1.domain.riot.dto.rune.RuneDto;
import song.mygg1.domain.riot.dto.rune.RuneStyleDto;
import song.mygg1.domain.riot.dto.summoner.SummonerDto;
import song.mygg1.domain.riot.dto.timeline.TimelineDto;

import java.util.List;
import java.util.Set;

@Slf4j
@Configuration
public class CacheConfig {

    @Bean
    public CacheService<List<ChampionWinRatePerDateDto>> championWinRateCacheService(
            StringRedisTemplate redis,
            ObjectMapper mapper
    ) {
        JavaType type = mapper.getTypeFactory()
                .constructCollectionType(List.class, ChampionWinRatePerDateDto.class);

        return new BaseCacheService<>(redis, mapper, type);
    }

    @Bean
    public CacheService<AggregatedCoreItemStatsDto> championItemBuildCacheService(
            StringRedisTemplate redis,
            ObjectMapper mapper
    ) {
        JavaType type = mapper.getTypeFactory()
                .constructType(AggregatedCoreItemStatsDto.class);

        return new BaseCacheService<>(redis, mapper, type);
    }

    @Bean
    public CacheService<ChampionLevelSkillStatsResponse> championSkillTreeCacheService(
            StringRedisTemplate redis,
            ObjectMapper mapper
    ) {
        JavaType type = mapper.getTypeFactory()
                .constructType(ChampionLevelSkillStatsResponse.class);

        return new BaseCacheService<>(redis, mapper, type);
    }

    @Bean
    public CacheService<ChampionRuneStatsResponse> championRuneCacheService(
            StringRedisTemplate redis,
            ObjectMapper mapper
    ) {
        JavaType type = mapper.getTypeFactory()
                .constructType(ChampionRuneStatsResponse.class);

        return new BaseCacheService<>(redis, mapper, type);
    }

    @Bean
    public CacheService<ChampionBanPickDto> championBanPickCacheService(
            StringRedisTemplate redis,
            ObjectMapper mapper
    ) {
        JavaType type = mapper.getTypeFactory()
                .constructType(ChampionBanPickDto.class);

        return new BaseCacheService<>(redis, mapper, type);
    }


    @Bean
    public CacheService<List<WinRateDto>> winRateDailyCacheService(
            StringRedisTemplate redis,
            ObjectMapper mapper
    ) {
        JavaType type = mapper.getTypeFactory()
                .constructCollectionType(List.class, WinRateDto.class);

        return new BaseCacheService<>(redis, mapper, type);
    }

    @Bean
    public CacheService<List<ChampionUsageDto>> championUsageCacheService(
            StringRedisTemplate redis,
            ObjectMapper mapper
    ) {
        JavaType type = mapper.getTypeFactory()
                .constructCollectionType(List.class, ChampionUsageDto.class);

        return new BaseCacheService<>(redis, mapper, type);
    }

    @Bean
    public CacheService<AccountDto> accountCacheService(
            StringRedisTemplate redis,
            ObjectMapper mapper
    ) {
        JavaType type = mapper.getTypeFactory()
                .constructType(AccountDto.class);

        return new BaseCacheService<>(redis, mapper, type);
    }

    @Bean
    public CacheService<SummonerDto> summonerCacheService(
            StringRedisTemplate redis,
            ObjectMapper mapper
    ) {
        JavaType type = mapper.getTypeFactory()
                .constructType(SummonerDto.class);

        return new BaseCacheService<>(redis, mapper, type);
    }

    @Bean
    public CacheService<Set<LeagueEntryDto>> leagueEntrySetCacheService(
            StringRedisTemplate redis,
            ObjectMapper mapper
    ) {
        JavaType type = mapper.getTypeFactory()
                .constructCollectionType(Set.class, LeagueEntryDto.class);

        return new BaseCacheService<>(redis, mapper, type);
    }

    @Bean
    public CacheService<LeagueListDto> leagueListCacheService(
            StringRedisTemplate redis,
            ObjectMapper mapper
    ) {
        JavaType type = mapper.getTypeFactory()
                .constructType(LeagueListDto.class);

        return new BaseCacheService<>(redis, mapper, type);
    }

    @Bean
    public CacheService<MatchDto> matchCacheService(
            StringRedisTemplate redis,
            ObjectMapper mapper
    ) {
        JavaType type = mapper.getTypeFactory()
                .constructType(MatchDto.class);

        return new BaseCacheService<>(redis, mapper, type);
    }

    @Bean
    public CacheService<TimelineDto> timelineCacheService(
            StringRedisTemplate redis,
            ObjectMapper mapper
    ) {
        JavaType type = mapper.getTypeFactory()
                .constructType(TimelineDto.class);

        return new BaseCacheService<>(redis, mapper, type);
    }

    @Bean
    public CacheService<String> searchCacheService(
            StringRedisTemplate redis,
            ObjectMapper mapper
    ) {
        JavaType type = mapper.getTypeFactory()
                .constructType(String.class);

        return new BaseCacheService<>(redis, mapper, type);
    }

    @Bean
    public CacheService<ChampionDto> championCacheService(
            StringRedisTemplate redis,
            ObjectMapper mapper
    ) {
        JavaType type = mapper.getTypeFactory()
                .constructType(ChampionDto.class);

        return new BaseCacheService<>(redis, mapper, type);
    }

    @Bean
    public CacheService<ItemDto> itemCacheService(
            StringRedisTemplate redis,
            ObjectMapper mapper
    ) {
        JavaType type = mapper.getTypeFactory()
                .constructType(ItemDto.class);

        return new BaseCacheService<>(redis, mapper, type);
    }

    @Bean
    public CacheService<RuneStyleDto> runeStyleCacheService(
            StringRedisTemplate redis,
            ObjectMapper mapper
    ) {
        JavaType type = mapper.getTypeFactory()
                .constructType(RuneStyleDto.class);

        return new BaseCacheService<>(redis, mapper, type);
    }

    @Bean
    public CacheService<RuneDto> runeCacheService(
            StringRedisTemplate redis,
            ObjectMapper mapper
    ) {
        JavaType type = mapper.getTypeFactory()
                .constructType(RuneDto.class);

        return new BaseCacheService<>(redis, mapper, type);
    }

    @Bean
    public CacheService<List<ChampionMasteryRankingDto>> championMasteryRankingCacheService(
            StringRedisTemplate redis,
            ObjectMapper mapper
    ) {
        JavaType type = mapper.getTypeFactory()
                .constructCollectionType(List.class, ChampionMasteryRankingDto.class);

        return new BaseCacheService<>(redis, mapper, type);
    }

    @Bean
    public CacheService<ChampionRotationsDto> rotationCacheService(
            StringRedisTemplate redis,
            ObjectMapper mapper
    ) {
        JavaType type = mapper.getTypeFactory()
                .constructType(ChampionRotationsDto.class);

        return new BaseCacheService<>(redis, mapper, type);
    }

    @Bean
    public CacheService<LeagueItemSummonerDto> leagueItemCacheService(
            StringRedisTemplate redis,
            ObjectMapper mapper
    ) {
        JavaType type = mapper.getTypeFactory()
                .constructType(LeagueItemSummonerDto.class);

        return new BaseCacheService<>(redis, mapper, type);
    }

    @Bean
    public CacheService<ChampionMasteryDto> championMasterCacheService(
            StringRedisTemplate redis,
            ObjectMapper mapper
    ) {
        JavaType type = mapper.getTypeFactory()
                .constructType(ChampionMasteryDto.class);

        return new BaseCacheService<>(redis, mapper, type);
    }
}
