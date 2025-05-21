package song.mygg1.domain.riot.dto.item;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemDto {
    private String id;
    private String name;
    private String description;
    private String colloq;
    private String plaintext;
    private Boolean consumed;
    private Integer stacks;
    private Integer depth;
    private Boolean consumeOnFull;
    private Boolean inStore;
    private List<Integer> from = new ArrayList<>();
    private List<Integer> into = new ArrayList<>();
    private Integer specialRecipe;
    private ItemImageDto image;
    private ItemGoldDto gold;
    private Set<String> tags = new HashSet<>();
    private Map<Integer, Boolean> maps = new HashMap<>();
    private Map<String, Double> stats;
    private Map<String, String> effect;
}
