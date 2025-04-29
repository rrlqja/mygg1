package song.mygg1.domain.common.exception.riot.exceptions;

import song.mygg1.domain.common.exception.riot.RiotApiException;

public class SummonerNotFoundException extends RiotApiException {
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
