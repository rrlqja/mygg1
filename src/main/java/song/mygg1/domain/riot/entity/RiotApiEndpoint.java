package song.mygg1.domain.riot.entity;

public enum RiotApiEndpoint {
    GET_ACCOUNT("/riot/account/v1/accounts/by-riot-id/{gameName}/{tagLine}"),
    GET_ACCOUNT_BY_PUUID("/riot/account/v1/accounts/by-puuid/{puuid}"),
    GET_MATCHES("/lol/match/v5/matches/by-puuid/{puuid}/ids?start={start}&count={count}"),
    GET_MATCH("/lol/match/v5/matches/{matchId}"),
    GET_SUMMONER("/lol/summoner/v4/summoners/by-puuid/{puuid}"),
    GET_SUMMONER_BY_SUMMONER_ID("/lol/summoner/v4/summoners/{summonerId}"),
    GET_LEAGUE_ENTRY("/lol/league/v4/entries/by-puuid/{puuid}"),
    GET_LEAGUE_LIST("/lol/league/v4/challengerleagues/by-queue/{queue}"),
    GET_CHAMPION_MASTERY_TOP_BY_PUUID("/lol/champion-mastery/v4/champion-masteries/by-puuid/{puuid}/top?count={count}"),
    GET_CHAMPION_ROTATIONS("/lol/platform/v3/champion-rotations")
    ;

    private final String path;

    RiotApiEndpoint(String path) { this.path = path; }

    public String getPath() { return path; }
}
