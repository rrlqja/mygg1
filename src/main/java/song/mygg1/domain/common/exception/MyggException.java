package song.mygg1.domain.common.exception;

public class MyggException extends RuntimeException {
    public MyggException() {
        super();
    }

    public MyggException(String message) {
        super(message);
    }

    public MyggException(String message, Throwable cause) {
        super(message, cause);
    }
}
