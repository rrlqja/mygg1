package song.mygg1.domain.riot.entity.league;

public enum LeagueQueue {
    RANKED_SOLO_5x5("RANKED_SOLO_5x5"),
    RANKED_FLEX_SR("RANKED_FLEX_SR"),
    RANKED_FLEX_TT("RANKED_FLEX_TT"),
    ;

    private final String queue;

    LeagueQueue(String queue) {
        this.queue = queue;
    }

    public String getQueue() {
        return queue;
    }
}
