package song.mygg1.domain.riot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import song.mygg1.domain.riot.dto.SearchDto;
import song.mygg1.domain.riot.entity.Account;
import song.mygg1.domain.riot.entity.Matches;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {
    private final AccountService accountService;
    private final MatchService matchService;

    /**
     * @param gameName
     * @param tagLine
     * 1. account
     * 2. summoner
     * 3. match
     */
    @Transactional
    public SearchDto search(String gameName, String tagLine, Integer start, Integer count) {
        Account account = accountService.findAccountByGameNameAndTagLine(gameName, tagLine);

        List<Matches> matchesList = matchService.getMatchList(account.getPuuid(), start, count);

        return new SearchDto(account, matchesList);
    }
}
