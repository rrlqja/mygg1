package song.mygg1.domain.common.exception.riot.searchexception.exceptions;

import song.mygg1.domain.common.exception.riot.searchexception.SearchException;

public class AccountNotFoundException extends SearchException {
    public AccountNotFoundException() {
        super("Account Not Found Exception");
    }

    public AccountNotFoundException(String message) {
        super(message);
    }

    public AccountNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
