package song.mygg.domain.post.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import song.mygg.domain.post.entity.Post;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResSavePostDto {
    private Long id;

    public ResSavePostDto(Post post) {
        this.id = post.getId();
    }
}
