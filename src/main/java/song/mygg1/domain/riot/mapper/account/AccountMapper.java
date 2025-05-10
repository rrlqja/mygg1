package song.mygg1.domain.riot.mapper.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import song.mygg1.domain.riot.dto.account.AccountDto;
import song.mygg1.domain.riot.entity.account.Account;

import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class AccountMapper {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yy/MM/dd hh:mm");

    public AccountDto toDto(Account account) {
        AccountDto dto = new AccountDto();

        dto.setPuuid(account.getPuuid());
        dto.setGameName(account.getGameName());
        dto.setTagLine(account.getTagLine());
        dto.setLastRefreshDateTime(account.getLastRefreshDateTime().format(FORMATTER));
        dto.setLastRefreshDateTimeRaw(account.getLastRefreshDateTime());

        return dto;
    }

    public Account toEntity(AccountDto dto) {
        return Account.create(dto.getPuuid(), dto.getGameName(), dto.getTagLine());
    }
}
