package song.mygg.domain.post.dto.response;

import lombok.Getter;
import lombok.Setter;
import song.mygg.domain.post.entity.Post;

@Getter @Setter
public class ResponseSavePostDto {
    private Long id;

    public ResponseSavePostDto(Post post) {
        this.id = post.getId();
    }
}
