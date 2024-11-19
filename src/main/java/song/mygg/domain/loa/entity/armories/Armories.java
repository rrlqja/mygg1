package song.mygg.domain.loa.entity.armories;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Armories {
    private ArmoryProfile armoryProfile;
    private List<ArmoryEquipmentItem> armoryEquipmentItems = new ArrayList<>();
    private List<ArmoryAvatarsItem> armoryAvatarsItems = new ArrayList<>();
    private List<ArmorySkillsItem> armorySkillsItems = new ArrayList<>();
    private ArmoryEngraving armoryEngraving;
    private ArmoryCard armoryCard;
    private ArmoryGem armoryGem;
    private ArkPassive arkPassive;
    private ColosseumInfo colosseumInfo;
    private List<CollectiblesItem> collectiblesItems = new ArrayList<>();
}
