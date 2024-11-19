package song.mygg.domain.loa.entity.armories;

import java.util.List;
import lombok.Data;

@Data
public class EffectsItem{
	private String toolTip;
	private String description;
	private String icon;
	private String name;
	private Integer index;
	private List<ItemsItem> items;
	private List<Integer> cardSlots;
}