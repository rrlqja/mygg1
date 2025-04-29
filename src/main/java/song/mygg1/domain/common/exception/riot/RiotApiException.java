package song.mygg1.domain.common.exception.riot;

import song.mygg1.domain.common.exception.MyggException;

public class RiotApiException extends MyggException {
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
