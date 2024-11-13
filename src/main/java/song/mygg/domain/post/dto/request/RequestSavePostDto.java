package song.mygg.domain.post.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RequestSavePostDto {
    private String title;
    private String content;
    private String writerName;
}
