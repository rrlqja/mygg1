package song.mygg.domain.loa.entity.armories;

import lombok.Data;

@Data
public class TripodsItem{
	private Integer tier;
	private String tooltip;
	private Integer slot;
	private Integer level;
	private Boolean isSelected;
	private String icon;
	private String name;
}