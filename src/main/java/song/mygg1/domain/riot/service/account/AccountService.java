package song.mygg1.domain.riot.service.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import song.mygg1.domain.common.exception.riot.account.exceptions.AccountNotFoundException;
import song.mygg1.domain.redis.service.CacheService;
import song.mygg1.domain.riot.dto.account.AccountDto;
import song.mygg1.domain.riot.mapper.account.AccountMapper;
import song.mygg1.domain.riot.repository.account.AccountJpaRepository;
import song.mygg1.domain.riot.entity.account.Account;
import song.mygg1.domain.riot.service.ApiService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
    private final CacheService<AccountDto> cacheService;
    private final AccountJpaRepository accountRepository;
    private final ApiService apiService;
    private final AccountMapper accountMapper;

    private static final Duration ACCOUNT_TTL = Duration.ofHours(6);
    private static final String ACCOUNT_CACHE_KEY_PREFIX_PUUID = "account:puuid:";
    private static final String ACCOUNT_CACHE_KEY_PREFIX_GAMENAME = "account:gameName:";

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public AccountDto findAccountByGameNameAndTagLine(String gameName, String tagLine) {
        String gameNameCacheKey = ACCOUNT_CACHE_KEY_PREFIX_GAMENAME + gameName + "#" + tagLine;

        Optional<AccountDto> cachedDto = cacheService.get(gameNameCacheKey);
        if (cachedDto.isPresent()) {
            AccountDto dto = cachedDto.get();
            log.debug("Cache hit for Account DTO by gameName: {}", gameNameCacheKey);

            Duration CACHE_VALIDATION_INTERVAL = Duration.ofHours(1);
            LocalDateTime lastRefreshTime = dto.getLastRefreshDateTimeRaw();

            if (lastRefreshTime == null ||
                    Duration.between(lastRefreshTime, LocalDateTime.now(ZoneId.systemDefault())).compareTo(CACHE_VALIDATION_INTERVAL) > 0) {

                log.info("Cached Account DTO for {} is due for validation. Checking API for puuid: {}", gameNameCacheKey, dto.getPuuid());
                try {
                    AccountDto updatedDto = findAccountByPuuidAndUpdate(dto.getPuuid());

                    if (!gameNameCacheKey.equals(ACCOUNT_CACHE_KEY_PREFIX_GAMENAME + updatedDto.getGameName() + "#" + updatedDto.getTagLine())) {
                        log.info("GameName/TagLine changed for puuid: {}. Evicting old cache key: {}", updatedDto.getPuuid(), gameNameCacheKey);
                        cacheService.evict(gameNameCacheKey);
                    }

                    return updatedDto;
                } catch (AccountNotFoundException e) {
                    log.warn("Failed to validate/update cached account for puuid: {}. Evicting cache and re-throwing.", dto.getPuuid(), e);
                    cacheService.evict(gameNameCacheKey);
                    cacheService.evict(ACCOUNT_CACHE_KEY_PREFIX_PUUID + dto.getPuuid());
                    throw e;
                } catch (Exception e) {
                    log.error("Error during cache validation for puuid: {}. Returning stale cache for now.", dto.getPuuid(), e);
                    return dto;
                }
            }
            return dto;
        }
        log.debug("Cache miss for Account DTO by gameName: {}. Attempting to find or fetch.", gameNameCacheKey);

        Optional<Account> accountOptionalByGameName = accountRepository.findAccountByGameNameAndTagLine(gameName, tagLine);

        if (accountOptionalByGameName.isPresent()) {
            Account accountFromDbByGameName = accountOptionalByGameName.get();
            String puuidFromDb = accountFromDbByGameName.getPuuid();
            log.info("Found account in DB by gameName#tagLine: {}#{} (puuid: {}). Verifying with API.", gameName, tagLine, puuidFromDb);

            return findAccountByPuuidAndUpdate(puuidFromDb);
        } else {
            log.info("Account not found in DB by gameName#tagLine: {}#{}. Fetching from API by gameName.", gameName, tagLine);
            AccountDto accountDtoFromApiByGameName = apiService.getAccount(gameName, tagLine)
                    .orElseThrow(() -> new AccountNotFoundException("사용자를 찾을 수 없습니다. (API 조회 실패): " + gameName + "#" + tagLine));

            String puuidFromApi = accountDtoFromApiByGameName.getPuuid();
            log.info("Fetched account from API by gameName: {}#{} (puuid: {}). Ensuring DB and cache are up-to-date.",
                    accountDtoFromApiByGameName.getGameName(), accountDtoFromApiByGameName.getTagLine(), puuidFromApi);

            return findAccountByPuuidAndUpdate(puuidFromApi);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public AccountDto findAccountByPuuidAndUpdate(String puuid) {
        String puuidCacheKey = ACCOUNT_CACHE_KEY_PREFIX_PUUID + puuid;

        AccountDto accountDtoFromApi = apiService.getAccount(puuid)
                .orElseThrow(() -> new AccountNotFoundException("PUUID로 사용자를 찾을 수 없습니다. (API 조회 실패): " + puuid));

        Account accountEntity = accountRepository.findById(puuid)
                .map(existingAccount -> {
                    log.info("Updating existing account for puuid: {} with API data ({}#{} -> {}#{}).",
                            puuid, existingAccount.getGameName(), existingAccount.getTagLine(),
                            accountDtoFromApi.getGameName(), accountDtoFromApi.getTagLine());
                    existingAccount.update(accountDtoFromApi.getGameName(), accountDtoFromApi.getTagLine());
                    return existingAccount;
                })
                .orElseGet(() -> {
                    log.info("No existing account for puuid: {}. Creating new account with API data ({}#{}).",
                            puuid, accountDtoFromApi.getGameName(), accountDtoFromApi.getTagLine());
                    return accountMapper.toEntity(accountDtoFromApi);
                });

        Account savedAccount = accountRepository.save(accountEntity);
        AccountDto resultDto = accountMapper.toDto(savedAccount);

        cacheService.put(puuidCacheKey, resultDto, ACCOUNT_TTL);
        String gameNameCacheKey = ACCOUNT_CACHE_KEY_PREFIX_GAMENAME + resultDto.getGameName() + "#" + resultDto.getTagLine();
        cacheService.put(gameNameCacheKey, resultDto, ACCOUNT_TTL);

        log.debug("Account updated and cached for puuid: {}, gameName: {}#{}", puuid, resultDto.getGameName(), resultDto.getTagLine());
        return resultDto;
    }

    @Transactional(readOnly = true)
    public AccountDto findAccountByPuuid(String puuid) {
        String puuidCacheKey = ACCOUNT_CACHE_KEY_PREFIX_PUUID + puuid;
        return cacheService.getOrLoad(
                puuidCacheKey,
                () -> {
                    Account entity = accountRepository.findById(puuid)
                            .orElseGet(() -> {
                                log.warn("Account not in DB for puuid: {}. Fetching from API.", puuid);
                                AccountDto dtoFromApi = apiService.getAccount(puuid)
                                        .orElseThrow(() -> new AccountNotFoundException("PUUID로 사용자를 찾을 수 없습니다. (API 조회 실패): " + puuid));
                                return accountRepository.save(accountMapper.toEntity(dtoFromApi));
                            });
                    return accountMapper.toDto(entity);
                },
                ACCOUNT_TTL
        );
    }
}
