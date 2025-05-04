package song.mygg1.domain.common.exception.riot.match.exceptions;

import song.mygg1.domain.common.exception.riot.match.MatchException;

public class MatchNotFoundException extends MatchException {
    public MatchNotFoundException() {
        super("Match Not Found Exception");
    }

    public MatchNotFoundException(String message) {
        super(message);
    }

    public MatchNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
