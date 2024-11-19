package song.mygg.domain.loa.entity.armories;

import java.util.List;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArmoryProfile{
	private Integer townLevel;
	private Integer totalSkillPoint;
	private String characterImage;
	private Integer characterLevel;
	private String townName;
	private String title;
	private String characterClassName;
	private String itemMaxLevel;
	private String itemAvgLevel;
	private String serverName;
	private List<StatsItem> stats;
	private String characterName;
	private String guildName;
	private List<TendenciesItem> tendencies;
	private Integer usingSkillPoint;
	private String pvpGradeName;
	private Integer expeditionLevel;
	private String guildMemberGrade;
}