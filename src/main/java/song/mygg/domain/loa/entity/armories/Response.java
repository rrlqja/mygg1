package song.mygg.domain.loa.entity.armories;

import java.util.List;
import lombok.Data;

@Data
public class Response{
	private ArmoryGem armoryGem;
	private ArkPassive arkPassive;
	private ColosseumInfo colosseumInfo;
	private List<ArmoryEquipmentItem> armoryEquipment;
	private ArmoryProfile armoryProfile;
	private ArmoryEngraving armoryEngraving;
	private List<ArmoryAvatarsItem> armoryAvatars;
	private ArmoryCard armoryCard;
	private List<CollectiblesItem> collectibles;
	private List<ArmorySkillsItem> armorySkills;
}