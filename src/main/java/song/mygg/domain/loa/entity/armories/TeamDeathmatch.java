package song.mygg.domain.loa.entity.armories;

import lombok.Data;

@Data
public class TeamDeathmatch{
	private Integer tieCount;
	private Integer victoryCount;
	private Integer playCount;
	private Integer loseCount;
	private Integer deathCount;
	private Integer killCount;
	private Integer aceCount;
}