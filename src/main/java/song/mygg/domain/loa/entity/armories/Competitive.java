package song.mygg.domain.loa.entity.armories;

import lombok.Data;

@Data
public class Competitive{
	private Integer tieCount;
	private Integer rankLastMmr;
	private Integer victoryCount;
	private Integer playCount;
	private Integer loseCount;
	private Integer deathCount;
	private Integer killCount;
	private Integer rank;
	private String rankName;
	private String rankIcon;
	private Integer aceCount;
}