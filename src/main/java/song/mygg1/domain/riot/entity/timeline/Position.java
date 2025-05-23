package song.mygg1.domain.riot.entity.timeline;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import song.mygg1.domain.riot.dto.timeline.PositionDto;

@Getter @Setter
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
