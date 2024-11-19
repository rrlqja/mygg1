package song.mygg.domain.loa.entity.armories;

import java.util.List;
import lombok.Data;

@Data
public class ArmorySkillsItem{
	private String type;
	private String tooltip;
	private List<TripodsItem> tripods;
	private Integer level;
	private Integer skillType;
	private String icon;
	private Object rune;
	private String name;
}