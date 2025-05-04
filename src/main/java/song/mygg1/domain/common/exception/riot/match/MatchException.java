package song.mygg1.domain.common.exception.riot.match;

import song.mygg1.domain.common.exception.MyggException;

public class MatchException extends MyggException {
    public MatchException() {
        super("Match Exception");
    }

    public MatchException(String message) {
        super(message);
    }

    public MatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
