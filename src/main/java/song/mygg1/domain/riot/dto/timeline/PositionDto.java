package song.mygg1.domain.riot.dto.timeline;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import song.mygg1.domain.riot.entity.timeline.Position;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PositionDto {
    private Integer x;
    private Integer y;

    public PositionDto(Position position) {
        this.x = position.getX();
        this.y = position.getY();
    }
}
