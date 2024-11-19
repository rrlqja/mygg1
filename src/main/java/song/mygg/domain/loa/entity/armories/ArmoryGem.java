package song.mygg.domain.loa.entity.armories;

import java.util.List;
import lombok.Data;

@Data
public class ArmoryGem{
	private List<GemsItem> gems;
	private Effects effects;
}