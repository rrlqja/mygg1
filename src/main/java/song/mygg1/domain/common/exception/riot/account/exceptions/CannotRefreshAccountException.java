package song.mygg1.domain.common.exception.riot.account.exceptions;

import song.mygg1.domain.common.exception.riot.account.AccountException;

public class CannotRefreshAccountException extends AccountException {
    public CannotRefreshAccountException() {
        super("Cannot Refresh Account Exception");
    }

    public CannotRefreshAccountException(String message) {
        super(message);
    }

    public CannotRefreshAccountException(String message, Throwable cause) {
        super(message, cause);
    }
}
