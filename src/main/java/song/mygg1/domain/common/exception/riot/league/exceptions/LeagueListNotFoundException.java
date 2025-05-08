package song.mygg1.domain.common.exception.riot.league.exceptions;

import song.mygg1.domain.common.exception.riot.league.LeagueException;

public class LeagueListNotFoundException extends LeagueException {
    public LeagueListNotFoundException() {
        super("LeagueList Not Found Exception");
    }

    public LeagueListNotFoundException(String message) {
        super(message);
    }

    public LeagueListNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
