package song.mygg1.domain.common.exception.riot;

import song.mygg1.domain.common.exception.MyggException;

public class SearchException extends MyggException {
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
