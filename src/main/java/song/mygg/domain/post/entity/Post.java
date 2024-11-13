package song.mygg.domain.post.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import song.mygg.domain.common.entity.CommonEntity;
import song.mygg.domain.user.entity.User;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends CommonEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;

    @JoinColumn(name = "writer_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private User writer;

    private String writerName;

    private Post(String title, String content, User writer, String writerName) {
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.writerName = writerName;
    }

    public static Post of(String title, String content, User writer, String writerName) {
        return new Post(title, content, writer, writerName);
    }
}
