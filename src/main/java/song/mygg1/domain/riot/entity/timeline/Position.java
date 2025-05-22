package song.mygg1.domain.riot.entity.timeline;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import song.mygg1.domain.riot.dto.timeline.PositionDto;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Position {
    private Integer x;
    private Integer y;

    public Position(PositionDto dto) {
        this.x = dto.getX();
        this.y = dto.getY();
    }
}
