package song.mygg.domain.common.exception.post;

public class PostException extends RuntimeException {
    public PostException() {
        super("Post Exception");
    }

    public PostException(String message) {
        super(message);
    }
}
