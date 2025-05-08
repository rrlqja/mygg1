package song.mygg1.domain.common.exception.riot.league;

import song.mygg1.domain.common.exception.riot.riotapi.RiotApiException;

public class LeagueException extends RiotApiException {
    public LeagueException() {
        super("League Exception");
    }

    public LeagueException(String message) {
        super(message);
    }

    public LeagueException(String message, Throwable cause) {
        super(message, cause);
    }
}
