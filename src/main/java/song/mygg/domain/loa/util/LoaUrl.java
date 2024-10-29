package song.mygg.domain.loa.util;

import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

public enum LoaUrl {
    GET_ARMORIES_PROFILE("https://developer-lostark.game.onstove.com/armories/characters/{characterName}/profiles");
//    GET_ADV_PROFILE("/getAdv/{name}/profile");

    private final String url;

    LoaUrl(String url) {
        this.url = url;
    }

    public String buildUrl(Map<String, Object> queryParams) {
        UriComponents uriBuilder = UriComponentsBuilder.fromHttpUrl(url).buildAndExpand(queryParams);

        return uriBuilder.toUriString();
    }
}
