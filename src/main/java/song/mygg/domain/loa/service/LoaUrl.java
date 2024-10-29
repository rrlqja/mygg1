package song.mygg.domain.loa.service;

import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

public enum LoaUrl {
    GET_ARMORIES_PROFILE("https://developer-lostark.game.onstove.com/armories/characters/{characterName}/profiles");
//    GET_ADV_PROFILE("/getAdv/{name}/profile");

    private final String urlTemplate;

    LoaUrl(String urlTemplate) {
        this.urlTemplate = urlTemplate;
    }

    public String buildUrl(Map<String, Object> queryParams) {
        UriComponents uriBuilder = UriComponentsBuilder.fromHttpUrl(urlTemplate).buildAndExpand(queryParams);

        return uriBuilder.toUriString();
    }
}
