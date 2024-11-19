package song.mygg.domain.loa.entity.armories;

import lombok.Data;

@Data
public class TeamElimination{
	private Integer playCount;
	private Integer loseCount;
	private Integer secondWinCount;
	private Integer killCount;
	private Integer thirdWinCount;
	private Integer secondPlayCount;
	private Integer tieCount;
	private Integer firstWinCount;
	private Integer firstPlayCount;
	private Integer thirdPlayCount;
	private Integer victoryCount;
	private Integer deathCount;
	private Integer allKillCount;
	private Integer aceCount;
}