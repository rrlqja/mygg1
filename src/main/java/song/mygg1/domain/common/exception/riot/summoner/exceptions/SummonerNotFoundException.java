package song.mygg1.domain.common.exception.riot.summoner.exceptions;

import song.mygg1.domain.common.exception.riot.SearchException;

public class SummonerNotFoundException extends SearchException {
    public SummonerNotFoundException() {
        super("Summoner Not Found Exception");
    }

    public SummonerNotFoundException(String message) {
        super(message);
    }

    public SummonerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
