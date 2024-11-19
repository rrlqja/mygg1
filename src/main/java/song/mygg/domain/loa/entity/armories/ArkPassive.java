package song.mygg.domain.loa.entity.armories;

import java.util.List;
import lombok.Data;

@Data
public class ArkPassive{
	private List<PointsItem> points;
	private Boolean isArkPassive;
	private List<EffectsItem> effects;
}