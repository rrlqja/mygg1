package song.mygg1.domain.common.exception.riot.exceptions;

import song.mygg1.domain.common.exception.riot.RiotApiException;

public class AccountNotFoundException extends RiotApiException {
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
