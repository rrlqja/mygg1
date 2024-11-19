package song.mygg.domain.loa.entity.armories;

import java.util.List;
import lombok.Data;

@Data
public class SkillsItem{
	private List<String> description;
	private Integer gemSlot;
	private String tooltip;
	private String option;
	private String icon;
	private String name;
}