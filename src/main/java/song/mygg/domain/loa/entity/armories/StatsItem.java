package song.mygg.domain.loa.entity.armories;

import java.util.List;
import lombok.Data;

@Data
public class StatsItem{
	private String type;
	private List<String> tooltip;
	private String value;
}