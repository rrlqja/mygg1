package song.mygg1.domain.common.exception.riot.summoner;

import song.mygg1.domain.common.exception.riot.SearchException;

public class SummonerException extends SearchException {
    public SummonerException() {
        super("Summoner Exception");
    }

    public SummonerException(String message) {
        super(message);
    }

    public SummonerException(String message, Throwable cause) {
        super(message, cause);
    }
}
