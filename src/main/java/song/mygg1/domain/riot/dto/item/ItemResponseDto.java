package song.mygg1.domain.riot.dto.item;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemResponseDto {
    private Map<String, ItemDto> data = new HashMap<>();

    @JsonProperty("data")
    public void setData(Map<String, ItemDto> data) {
        data.forEach((key, item) -> item.setId(key));
        this.data = data;
    }
}
