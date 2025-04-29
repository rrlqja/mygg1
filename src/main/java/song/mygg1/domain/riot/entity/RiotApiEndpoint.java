package song.mygg1.domain.riot.entity;

public enum RiotApiEndpoint {
    GET_ACCOUNT("/riot/account/v1/accounts/by-riot-id/{gameName}/{tagLine}"),
    GET_MATCHES("/lol/match/v5/matches/by-puuid/{puuid}/ids?start={start}&count={count}"),
    GET_MATCH("/lol/match/v5/matches/{matchId}")
    ;

    private final String path;

    RiotApiEndpoint(String path) { this.path = path; }

    public String getPath() { return path; }
}
