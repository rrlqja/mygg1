package song.mygg.domain.loa.entity.armories;

import java.util.List;
import lombok.Data;

@Data
public class ArmoryCard{
	private List<EffectsItem> effects;
	private List<CardsItem> cards;
}