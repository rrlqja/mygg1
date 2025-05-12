package song.mygg1.domain.common.exception.riot.match;

import song.mygg1.domain.common.exception.riot.SearchException;

public class MatchException extends SearchException {
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
