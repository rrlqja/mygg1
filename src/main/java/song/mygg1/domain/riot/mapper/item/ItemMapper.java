package song.mygg1.domain.riot.mapper.item;

import org.springframework.stereotype.Component;
import song.mygg1.domain.riot.dto.item.ItemDto;
import song.mygg1.domain.riot.dto.item.ItemGoldDto;
import song.mygg1.domain.riot.dto.item.ItemImageDto;
import song.mygg1.domain.riot.entity.item.Item;
import song.mygg1.domain.riot.entity.item.ItemGold;
import song.mygg1.domain.riot.entity.item.ItemImage;

@Component
public class ItemMapper {
    public ItemDto toDto(Item item) {
        ItemImage image = item.getImage();
        ItemImageDto imageDto = new ItemImageDto(image.getFull(), image.getSprite(), image.getGroup(), image.getX(), image.getY(), image.getW(), image.getH());

        ItemGold gold = item.getGold();
        ItemGoldDto goldDto = new ItemGoldDto(gold.getBase(), gold.getPurchasable(), gold.getTotal(), gold.getSell());

        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.getColloq(), item.getPlaintext(), item.getConsumed(), item.getStacks(), item.getDepth(), item.getConsumeOnFull(), item.getInStore(),
                item.getFrom(), item.getInto(), item.getSpecialRecipe(), imageDto, goldDto, item.getTags(), item.getMaps(), item.getStats(), item.getEffect());
    }

    public Item toEntity(ItemDto dto) {
        ItemImageDto imageDto = dto.getImage();
        ItemImage image = ItemImage.create(imageDto.getFull(), imageDto.getSprite(), imageDto.getSprite(), imageDto.getX(), imageDto.getY(), imageDto.getW(), imageDto.getH());

        ItemGoldDto goldDto = dto.getGold();
        ItemGold gold = ItemGold.creat(goldDto.getBase(), goldDto.getPurchasable(), goldDto.getTotal(), goldDto.getSell());

        return Item.create(
                dto.getId(),
                dto.getName(),
                dto.getDescription(),
                dto.getColloq(),
                dto.getPlaintext(),
                dto.getConsumed(),
                dto.getStacks(),
                dto.getDepth(),
                dto.getConsumeOnFull(),
                dto.getInStore(),
                dto.getSpecialRecipe(),

                dto.getFrom(),
                dto.getInto(),
                image,
                gold,
                dto.getTags(),
                dto.getMaps(),
                dto.getStats(),
                dto.getEffect()
        );
    }
}
