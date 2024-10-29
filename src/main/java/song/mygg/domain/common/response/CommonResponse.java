package song.mygg.domain.common.response;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CommonResponse<T> {
    private Integer status;
    private String message;
    private T body;

    public CommonResponse(Integer status, String message, T body) {
        this.status = status;
        this.message = message;
        this.body = body;
    }
}
