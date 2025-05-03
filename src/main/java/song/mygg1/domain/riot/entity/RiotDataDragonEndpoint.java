package song.mygg1.domain.riot.entity;

public enum RiotDataDragonEndpoint {
    GET_DATA_DRAGON_VERSION("/api/versions.json"),
    GET_PROFILE_ICON("/cdn/{version}/img/profileicon/{iconId}.png"),
    GET_CHAMPION("/cdn/{version}/img/champion/{championName}.png"),
    GET_ITEM("/cdn/{version}/img/item/{itemId}.png"),
    GET_SUMMONER_JSON("/cdn/{version}/data/ko_KR/summoner.json"),
    GET_SPELL_ICON("/cdn/{version}/img/{group}/{spell}")
    ;

    private final String path;

    RiotDataDragonEndpoint(String path) { this.path = path; }

    public String getPath() { return path; }
}
