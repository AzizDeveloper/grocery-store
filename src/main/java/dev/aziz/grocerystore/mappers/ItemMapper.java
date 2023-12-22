package dev.aziz.grocerystore.mappers;

import dev.aziz.grocerystore.dtos.ItemDto;
import dev.aziz.grocerystore.dtos.ItemSummaryDto;
import dev.aziz.grocerystore.entities.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface ItemMapper {

    @Mapping(target = "category", source = "categoryDto")
    Item itemDtoToItem(ItemDto itemDto);

    @Mapping(target = "categoryDto", source = "category")
    ItemDto itemToItemDto(Item item);

    @Mapping(target = "price", source = "price")
    @Mapping(target = "categoryDto", source = "category")
    ItemSummaryDto itemToItemSummaryDto(Item item);

    List<ItemDto> itemsToItemDtos(List<Item> items);

    @Mapping(target = "price", source = "price")
    List<ItemSummaryDto> itemsToItemSummaryDtos(List<Item> items);
}
