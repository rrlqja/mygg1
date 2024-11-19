package song.mygg.domain.loa.entity.armories;

import java.util.List;
import lombok.Data;

@Data
public class CollectiblesItem{
	private String type;
	private String icon;
	private Integer point;
	private Integer maxPoint;
	private List<CollectiblePointsItem> collectiblePoints;
}