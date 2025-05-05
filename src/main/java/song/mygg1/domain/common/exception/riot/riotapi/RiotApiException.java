package song.mygg1.domain.common.exception.riot.riotapi;

import song.mygg1.domain.common.exception.riot.SearchException;

public class RiotApiException extends SearchException {
    public RiotApiException() {
        super("Riot Api Exception");
    }

    public RiotApiException(String message) {
        super(message);
    }

    public RiotApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
