package song.mygg1.domain.common.exception.riot.account.exceptions;

import song.mygg1.domain.common.exception.riot.account.AccountException;

public class AccountNotFoundException extends AccountException {
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
