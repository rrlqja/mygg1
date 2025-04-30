package song.mygg1.domain.common.exception.riot.searchexception;

import song.mygg1.domain.common.exception.riot.RiotApiException;

public class SearchException extends RiotApiException {
    public SearchException() {
        super("Search Exception");
    }

    public SearchException(String message) {
        super(message);
    }

    public SearchException(String message, Throwable cause) {
        super(message, cause);
    }
}
