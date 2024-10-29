package song.mygg.domain.jsph.util;

import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

public enum JSPHUrl {
    GET_POST_BY_ID("https://jsonplaceholder.typicode.com/posts/{id}");

    private final String url;

    JSPHUrl(String url) {
        this.url = url;
    }

    public String buildUrl(Map<String, Object> queryParams) {
        UriComponents uriBuilder = UriComponentsBuilder.fromHttpUrl(url).buildAndExpand(queryParams);

        return uriBuilder.toUriString();
    }
}
