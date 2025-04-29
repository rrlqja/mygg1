package song.mygg1.domain.riot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import song.mygg1.domain.common.exception.riot.exceptions.AccountNotFoundException;
import song.mygg1.domain.riot.repository.AccountJpaRepository;
import song.mygg1.domain.riot.entity.Account;

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
                    Account account = apiService.getAccount(gameName, tagLine)
                            .orElseThrow(() -> {
                                log.warn("Riot Api: {}#{} 유저를 찾을 수 없습니다.", gameName, tagLine);
                                return new AccountNotFoundException("사용자를 찾을 수 없습니다.");
                            });
                    return accountRepository.save(account);
                });
    }
}
