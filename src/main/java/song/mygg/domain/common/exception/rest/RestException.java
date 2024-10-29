package song.mygg.domain.common.exception.rest;

public class RestException extends RuntimeException {
    public RestException() {
        super("Rest Exception");
    }

    public RestException(String message) {
        super(message);
    }
}
