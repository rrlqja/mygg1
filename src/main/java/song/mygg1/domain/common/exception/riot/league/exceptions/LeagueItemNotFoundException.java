package song.mygg1.domain.common.exception.riot.league.exceptions;

import song.mygg1.domain.common.exception.riot.league.LeagueException;

public class LeagueItemNotFoundException extends LeagueException {
    public LeagueItemNotFoundException() {
        super("League Item Not Found Exception");
    }

    public LeagueItemNotFoundException(String message) {
        super(message);
    }

    public LeagueItemNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
