package song.mygg.domain.loa.entity.armories;

import java.util.List;
import lombok.Data;

@Data
public class ArmoryEngraving{
	private Object engravings;
	private List<ArkPassiveEffectsItem> arkPassiveEffects;
	private Object effects;
}