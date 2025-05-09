package song.mygg1.domain.riot.entity.match;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamId implements Serializable {
    @Column(name = "info_id")
    private Long infoId;
    private Integer teamId;
}
