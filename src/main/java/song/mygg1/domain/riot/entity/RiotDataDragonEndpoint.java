package song.mygg1.domain.riot.entity;

public enum RiotDataDragonEndpoint {
    GET_DATA_DRAGON_VERSION("/api/versions.json"),
    GET_PROFILE_ICON("/cdn/{version}/img/profileicon/{iconId}.png"),
    GET_CHAMPION("/cdn/{version}/img/champion/{championName}.png"),
    GET_CHAMPION_JSON("/cdn/{version}/data/ko_KR/champion.json"),
    GET_ITEM("/cdn/{version}/img/item/{itemId}.png"),
    GET_SUMMONER_JSON("/cdn/{version}/data/ko_KR/summoner.json"),
    GET_ICON("/cdn/{version}/img/{group}/{full}")
    ;

    private final String path;

    RiotDataDragonEndpoint(String path) { this.path = path; }

    public String getPath() { return path; }
}
