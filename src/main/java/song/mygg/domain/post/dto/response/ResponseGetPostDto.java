package song.mygg.domain.post.dto.response;

import lombok.Getter;
import lombok.Setter;
import song.mygg.domain.post.entity.Post;
import song.mygg.domain.user.dto.response.ResponseUserDto;

@Getter @Setter
public class ResponseGetPostDto {
    private Long postId;
    private String title;
    private String content;
    private String writerName;
    private ResponseUserDto userDto;

    public ResponseGetPostDto(Post post) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        if (post.getWriter() != null) {
            this.userDto = new ResponseUserDto(post.getWriter());
        } else {
            writerName = post.getWriterName();
        }
    }
}
