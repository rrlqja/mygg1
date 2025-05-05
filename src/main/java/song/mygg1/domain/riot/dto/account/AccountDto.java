package song.mygg1.domain.riot.dto.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import song.mygg1.domain.riot.entity.account.Account;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    private String puuid;
    private String gameName;
    private String tagLine;
    private LocalDateTime lastRefreshDateTimeRaw;
    private String lastRefreshDateTime;

    public Account toEntity() {
        return Account.create(puuid, gameName, tagLine);
    }

    public AccountDto(Account account) {
        this.puuid = account.getPuuid();
        this.gameName = account.getGameName();
        this.tagLine = account.getTagLine();
        this.lastRefreshDateTime = account.getLastRefreshDateTime().format(DateTimeFormatter.ofPattern("yy/MM/dd hh:mm"));
        this.lastRefreshDateTimeRaw = account.getLastRefreshDateTime();
    }
}
