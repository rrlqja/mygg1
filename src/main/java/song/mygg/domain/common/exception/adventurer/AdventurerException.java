package song.mygg.domain.common.exception.adventurer;

public class AdventurerException extends RuntimeException {
    public AdventurerException() {
        super("Adventurer Exception");
    }

    public AdventurerException(String message) {
        super(message);
    }
}
