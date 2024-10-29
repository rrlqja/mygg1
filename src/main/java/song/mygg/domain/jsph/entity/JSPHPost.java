package song.mygg.domain.jsph.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import song.mygg.domain.common.entity.CommonEntity;

@Entity
@Getter @Setter
@ToString(exclude = {"postId"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JSPHPost extends CommonEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long postId;

    private long id;
    private long userId;
    private String title;
    private String body;

    public static JSPHPost of() {
        return new JSPHPost();
    }
}
