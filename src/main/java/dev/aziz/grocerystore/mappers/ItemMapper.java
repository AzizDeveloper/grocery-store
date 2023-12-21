package dev.aziz.grocerystore.mappers;

import dev.aziz.grocerystore.dtos.ItemDto;
import dev.aziz.grocerystore.dtos.ItemSummaryDto;
import dev.aziz.grocerystore.entities.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    Item itemDtoToItem(ItemDto itemDto);

    ItemDto itemToItemDto(Item item);

    @Mapping(target = "price", source = "price")
    ItemSummaryDto itemToItemSummaryDto(Item item);

    List<ItemDto> itemsToItemDtos(List<Item> items);

    @Mapping(target = "price", source = "price")
    List<ItemSummaryDto> itemsToItemSummaryDtos(List<Item> items);
}
