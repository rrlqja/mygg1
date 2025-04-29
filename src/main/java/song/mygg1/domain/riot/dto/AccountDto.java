package song.mygg1.domain.riot.dto;

import lombok.Data;
import song.mygg1.domain.riot.entity.Account;

@Data
public class AccountDto {
    private String gameName;
    private String tagLine;

    public AccountDto(Account account) {
        this.gameName = account.getGameName();
        this.tagLine = account.getTagLine();
    }
}
