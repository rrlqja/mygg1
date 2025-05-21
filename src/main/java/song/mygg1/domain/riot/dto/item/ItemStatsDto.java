package song.mygg1.domain.riot.dto.item;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemStatsDto {
    private Map<String, Double> stats = new HashMap<>();

    @JsonAnySetter
    public void addStat(String key, Double value) {
        stats.put(key, value);
    }

    @JsonAnyGetter
    public Map<String, Double> getStats() {
        return stats;
    }
}
