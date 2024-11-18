package song.mygg.domain.post.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import song.mygg.domain.post.entity.Post;
import song.mygg.domain.user.entity.User;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResGetPostDto {
    private Long postId;
    private String title;
    private String content;
    private String writerName;
    private ResGetPostWriterDto userDto;

    public ResGetPostDto(Post post) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        if (post.getWriter() != null) {
            this.userDto = new ResGetPostWriterDto(post.getWriter());
        } else {
            writerName = post.getWriterName();
        }
    }

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class ResGetPostWriterDto {
        private String writer;

        public ResGetPostWriterDto(User user) {
            this.writer = user.getUsername();
        }
    }
}
