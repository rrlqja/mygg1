package song.mygg1.domain.common.exception.riot.account;

import song.mygg1.domain.common.exception.riot.SearchException;

public class AccountException extends SearchException {
    public AccountException() {
        super("Account Exception");
    }

    public AccountException(String message) {
        super(message);
    }

    public AccountException(String message, Throwable cause) {
        super(message, cause);
    }
}
