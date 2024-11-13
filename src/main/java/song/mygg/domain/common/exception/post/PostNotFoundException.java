package song.mygg.domain.common.exception.post;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException() {
        super("Post Not Found Exception");
    }

    public PostNotFoundException(String message) {
        super(message);
    }
}
