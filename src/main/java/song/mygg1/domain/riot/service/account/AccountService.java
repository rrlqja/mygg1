package song.mygg1.domain.riot.service.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import song.mygg1.domain.common.exception.riot.account.exceptions.AccountNotFoundException;
import song.mygg1.domain.common.exception.riot.account.exceptions.CannotRefreshAccountException;
import song.mygg1.domain.riot.dto.account.AccountDto;
import song.mygg1.domain.riot.repository.account.AccountJpaRepository;
import song.mygg1.domain.riot.entity.account.Account;
import song.mygg1.domain.riot.service.ApiService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountJpaRepository accountRepository;
    private final ApiService apiService;

    @Transactional
    public Account findAccountByGameNameAndTagLine(String gameName, String tagLine) {
        return accountRepository.findAccountByGameNameAndTagLine(gameName, tagLine)
                .orElseGet(() -> {
                    AccountDto accountDto = apiService.getAccount(gameName, tagLine)
                            .orElseThrow(() -> {
                                log.warn("Riot Api: {}#{} 유저를 찾을 수 없습니다.", gameName, tagLine);
                                return new AccountNotFoundException("사용자를 찾을 수 없습니다.");
                            });
                    return accountRepository.save(accountDto.toEntity());
                });
    }

    @Transactional
    public Account refreshAccount(String puuid) {
        AccountDto puuidAccount = apiService.getAccount(puuid)
                .orElseThrow(AccountNotFoundException::new);

        Account account = accountRepository.findAccountByPuuid(puuid)
                .orElseThrow();

        if (account.getLastRefreshDateTime() != null && Duration.between(account.getLastRefreshDateTime(), LocalDateTime.now()).toMinutes() < 5) {
            throw new CannotRefreshAccountException("전적 갱신을 할 수 없습니다.");
        }

        account.update(puuidAccount.getGameName(), puuidAccount.getTagLine());
        return accountRepository.save(account);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Account> findAccountList(List<String> puuidlist) {
        List<Account> existingAccountList = accountRepository.findAccountsByPuuidIn(puuidlist);

        Set<String> found = existingAccountList.stream()
                .map(Account::getPuuid)
                .collect(Collectors.toSet());
        List<String> missingAccountList = puuidlist.stream()
                .filter(p -> !found.contains(p))
                .toList();

        List<AccountDto> fetchDtoList = missingAccountList.stream()
                .map(apiService::getAccount)
                .flatMap(Optional::stream)
                .toList();

        List<Account> fetchedEntities = fetchDtoList.stream()
                .map(AccountDto::toEntity)
                .toList();

        List<Account> saved = accountRepository.saveAll(fetchedEntities);

        existingAccountList.addAll(saved);
        return existingAccountList;
    }
}
