package song.mygg.domain.jsph.util;

public enum JSPHUrl {
    getPostById("https://jsonplaceholder.typicode.com/posts/");

    private final String url;

    JSPHUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
