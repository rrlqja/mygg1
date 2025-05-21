package song.mygg1.domain.riot.entity.item;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemGold {
    private Integer base;
    private Boolean purchasable;
    private Integer total;
    private Integer sell;

    public static ItemGold creat(Integer base, Boolean purchasable, Integer total, Integer sell) {
        return new ItemGold(base, purchasable, total, sell);
    }

    private ItemGold(Integer base, Boolean purchasable, Integer total, Integer sell) {
        this.base = base;
        this.purchasable = purchasable;
        this.total = total;
        this.sell = sell;
    }
}
