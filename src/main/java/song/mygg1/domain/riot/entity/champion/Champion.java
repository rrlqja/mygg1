package song.mygg1.domain.riot.entity.champion;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Champion {
    @Id @Column(name = "champion_key")
    private Long key;
    private String id;
    private String name;
    private String title;
    @Column(length = 4096)
    private String blurb;

    public static Champion create(Long key, String id, String name, String title, String blurb) {
        return new Champion(key, id, name, title, blurb);
    }

    private Champion(Long key, String id, String name, String title, String blurb) {
        this.key = key;
        this.id = id;
        this.name = name;
        this.title = title;
        this.blurb = blurb;
    }
}
