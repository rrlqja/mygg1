package song.mygg1.domain.riot.dto.timeline;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import song.mygg1.domain.riot.entity.timeline.Position;

@Getter @Setter
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
