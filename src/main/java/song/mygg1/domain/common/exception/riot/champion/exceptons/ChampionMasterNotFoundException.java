package song.mygg1.domain.common.exception.riot.champion.exceptons;

import song.mygg1.domain.common.exception.riot.champion.ChampionException;

public class ChampionMasterNotFoundException extends ChampionException {
    public ChampionMasterNotFoundException() {
        super("Champion Master Not Found Exception");
    }

    public ChampionMasterNotFoundException(String message) {
        super(message);
    }

    public ChampionMasterNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
