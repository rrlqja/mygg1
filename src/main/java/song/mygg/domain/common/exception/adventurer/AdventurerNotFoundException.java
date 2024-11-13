package song.mygg.domain.common.exception.adventurer;

public class AdventurerNotFoundException extends AdventurerException {
    public AdventurerNotFoundException() {
        super("Adventurer Not Found Exception");
    }

    public AdventurerNotFoundException(String message) {
        super(message);
    }
}
