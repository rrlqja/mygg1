package song.mygg1.domain.common.exception.riot.champion.exceptons;

import song.mygg1.domain.common.exception.riot.champion.ChampionException;

public class ChampionNotFoundException extends ChampionException {
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
