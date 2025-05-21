package song.mygg1.domain.riot.entity.item;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import song.mygg1.domain.riot.dto.item.ItemGoldDto;
import song.mygg1.domain.riot.dto.item.ItemImageDto;
import song.mygg1.domain.riot.dto.item.ItemStatsDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {
    @Id
    private String id;
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String colloq;
    private String plaintext;
    private Boolean consumed;
    private Integer stacks;
    private Integer depth;
    private Boolean consumeOnFull;
    private Boolean inStore;
    private Integer specialRecipe;

    @ElementCollection
    @CollectionTable(name = "item_from", joinColumns = @JoinColumn(name = "item_id"))
    @Column(name = "from_item_id", length = 10)
    private List<Integer> from = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "item_into", joinColumns = @JoinColumn(name = "item_id"))
    @Column(name = "into_item_id", length = 10)
    private List<Integer> into = new ArrayList<>();

    @Embedded
    private ItemImage image;

    @Embedded
    private ItemGold gold;

    @ElementCollection
    @CollectionTable(name = "item_tags", joinColumns = @JoinColumn(name = "item_id"))
    @Column(name = "tag")
    private Set<String> tags;

    @ElementCollection
    @CollectionTable(name = "item_maps", joinColumns = @JoinColumn(name = "item_id"))
    @MapKeyColumn(name = "map_id", length = 4)
    @Column(name = "available")
    private Map<Integer, Boolean> maps = new HashMap<>();

    @ElementCollection
    @CollectionTable(name = "item_stats", joinColumns = @JoinColumn(name = "item_id"))
    @MapKeyColumn(name = "stat_key")
    @Column(name = "stat_value")
    private Map<String, Double> stats;

    @ElementCollection
    @CollectionTable(name = "item_effect", joinColumns = @JoinColumn(name = "item_id"))
    @MapKeyColumn(name = "effect_key")
    @Column(name = "effect_value")
    private Map<String, String> effect;

    public static Item create(String id, String name, String description, String colloq, String plaintext, Boolean consumed, Integer stacks, Integer depth, Boolean consumeOnFull, Boolean inStore, Integer specialRecipe,
                              List<Integer> from, List<Integer> into, ItemImage image, ItemGold gold, Set<String> tags, Map<Integer, Boolean> maps, Map<String, Double> stats, Map<String, String> effect) {
        return new Item(id, name, description, colloq, plaintext, consumed, stacks, depth, consumeOnFull, inStore, specialRecipe,
                from, into, image, gold, tags, maps, stats, effect);
    }

    private Item(String id, String name, String description, String colloq, String plaintext, Boolean consumed, Integer stacks, Integer depth, Boolean consumeOnFull, Boolean inStore, Integer specialRecipe,
                 List<Integer> from, List<Integer> into, ItemImage image, ItemGold gold, Set<String> tags, Map<Integer, Boolean> maps, Map<String, Double> stats, Map<String, String> effect) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.colloq = colloq;
        this.plaintext = plaintext;
        this.consumed = consumed;
        this.stacks = stacks;
        this.depth = depth;
        this.consumeOnFull = consumeOnFull;
        this.inStore = inStore;
        this.specialRecipe = specialRecipe;

        this.from = from;
        this.into = into;
        this.image = image;
        this.gold = gold;
        this.tags = tags;
        this.maps = maps;
        this.stats = stats;
        this.effect = effect;
    }
}
