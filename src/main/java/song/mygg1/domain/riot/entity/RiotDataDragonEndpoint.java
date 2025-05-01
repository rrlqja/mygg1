package song.mygg1.domain.riot.entity;

public enum RiotDataDragonEndpoint {
    GET_DATA_DRAGON_VERSION("/api/versions.json"),
    GET_PROFILE_ICON("/cdn/{version}/img/profileicon/{iconId}.png"),
    GET_CHAMPION("/cdn/{version}/img/champion/{championName}.png")
    ;

    private final String path;

    RiotDataDragonEndpoint(String path) { this.path = path; }

    public String getPath() { return path; }
}
