package dev.aziz.grocerystore.handlers;

import dev.aziz.grocerystore.entities.BasketItem;
import dev.aziz.grocerystore.entities.UserPromotion;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PriceHandlerContext {

    private final List<UserPromotion> userPromotions;
    private final List<BasketItem> basketItems;

}
