package song.mygg.domain.loa.exception.adventurer;

public class AdventurerException extends RuntimeException {
    public AdventurerException() {
        super("Adventurer Exception");
    }

    public AdventurerException(String message) {
        super(message);
    }
}
