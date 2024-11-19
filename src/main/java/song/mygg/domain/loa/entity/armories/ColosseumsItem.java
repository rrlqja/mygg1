package song.mygg.domain.loa.entity.armories;

import lombok.Data;

@Data
public class ColosseumsItem{
	private Competitive competitive;
	private TeamElimination teamElimination;
	private String seasonName;
	private TeamDeathmatch teamDeathmatch;
	private Object oneDeathmatch;
	private Object coOpBattle;
}