package song.mygg1.domain.riot.dto.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import song.mygg1.domain.riot.entity.account.Account;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    private String puuid;
    private String gameName;
    private String tagLine;

    public Account toEntity() {
        return Account.create(puuid, gameName, tagLine);
    }

    public AccountDto(Account account) {
        puuid = account.getPuuid();
        this.gameName = account.getGameName();
        this.tagLine = account.getTagLine();
    }
}
