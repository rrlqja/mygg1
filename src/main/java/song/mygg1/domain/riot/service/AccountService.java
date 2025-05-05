package song.mygg1.domain.riot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import song.mygg1.domain.common.exception.riot.account.exceptions.AccountNotFoundException;
import song.mygg1.domain.common.exception.riot.account.exceptions.CannotRefreshAccountException;
import song.mygg1.domain.riot.dto.account.AccountDto;
import song.mygg1.domain.riot.repository.AccountJpaRepository;
import song.mygg1.domain.riot.entity.account.Account;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

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
}
