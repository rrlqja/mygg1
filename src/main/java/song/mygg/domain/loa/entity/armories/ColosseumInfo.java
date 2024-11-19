package song.mygg.domain.loa.entity.armories;

import java.util.List;
import lombok.Data;

@Data
public class ColosseumInfo{
	private Integer rank;
	private Integer exp;
	private List<ColosseumsItem> colosseums;
	private Integer preRank;
}