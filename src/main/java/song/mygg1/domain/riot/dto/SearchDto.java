package song.mygg1.domain.riot.dto;

import lombok.Data;
import song.mygg1.domain.riot.entity.Account;
import song.mygg1.domain.riot.entity.Matches;

import java.util.List;

@Data
public class SearchDto {
    private AccountDto account;
    private List<MatchDto> matchList;

    public SearchDto(Account account, List<Matches> matchesList) {
        this.account = new AccountDto(account);
        this.matchList = matchesList.stream().map(MatchDto::new).toList();
    }
}
