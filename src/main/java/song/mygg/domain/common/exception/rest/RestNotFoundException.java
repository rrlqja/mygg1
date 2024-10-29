package song.mygg.domain.common.exception.rest;

public class RestNotFoundException extends RestException {
    public RestNotFoundException() {
        super("Rest Not Found Exception");
    }

    public RestNotFoundException(String message) {
        super(message);
    }
}
