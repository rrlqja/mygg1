package song.mygg.domain.user.dto.response;

import lombok.Getter;
import lombok.Setter;
import song.mygg.domain.user.entity.User;

@Getter @Setter
public class ResponseUserDto {
    private String username;

    public ResponseUserDto(User user) {
        this.username = user.getUsername();
    }
}
