package song.mygg1.domain.common.exception.riot.champion;

import song.mygg1.domain.common.exception.riot.SearchException;

public class ChampionException extends SearchException {
    public ChampionException() {
        super("Champion Exception");
    }

    public ChampionException(String message) {
        super(message);
    }

    public ChampionException(String message, Throwable cause) {
        super(message, cause);
    }
}
