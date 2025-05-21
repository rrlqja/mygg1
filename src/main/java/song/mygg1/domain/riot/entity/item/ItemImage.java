package song.mygg1.domain.riot.entity.item;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemImage {
    private String full;
    private String sprite;
    @Column(name = "item_image_group")
    private String group;
    private Integer x;
    private Integer y;
    private Integer w;
    private Integer h;

    public static ItemImage create(String full, String sprite, String group, Integer x, Integer y, Integer w, Integer h) {
        return new ItemImage(full, sprite, group, x, y, w, h);
    }

    private ItemImage(String full, String sprite, String group, Integer x, Integer y, Integer w, Integer h) {
        this.full = full;
        this.sprite = sprite;
        this.group = group;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
}
