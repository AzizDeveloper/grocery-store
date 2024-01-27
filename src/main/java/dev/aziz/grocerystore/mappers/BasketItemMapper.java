package dev.aziz.grocerystore.mappers;

import dev.aziz.grocerystore.dtos.BasketItemDto;
import dev.aziz.grocerystore.entities.BasketItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BasketItemMapper {

    @Mapping(source = "item.name", target = "name")
    @Mapping(source = "amount", target = "stockAmount")
    @Mapping(source = "item.price", target = "unitPrice")
    @Mapping(expression = "java(" +
            "BigDecimal.valueOf( basketItem.getAmount() ).multiply( basketItem.getItem().getPrice() ).toString()" +
            ")", target = "totalPrice")
    BasketItemDto basketItemToBasketItemDto(BasketItem basketItem);

    List<BasketItemDto> basketItemsToBasketItemDtos(List<BasketItem> basketItems);
}
