package song.mygg1.domain.common.exception.riot.champion;

import song.mygg1.domain.common.exception.riot.SearchException;

public class ChampionNotFoundException extends SearchException {
    public ChampionNotFoundException() {
        super("Champion Not Found Exception");
    }

    public ChampionNotFoundException(String message) {
        super(message);
    }

    public ChampionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
