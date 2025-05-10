package song.mygg1.domain.riot.dto.match;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChampionImgInfoDto {
//    @JsonProperty(value = "url")
    private String url;
//    @JsonProperty(value = "player")
    private boolean isPlayer;
}
