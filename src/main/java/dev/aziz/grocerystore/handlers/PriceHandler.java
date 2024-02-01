package dev.aziz.grocerystore.handlers;

import dev.aziz.grocerystore.entities.BasketItem;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

public class PriceHandler {

    private List<BasketItem> basketItems;

    public PriceHandler(List<BasketItem> basketItems) {
        this.basketItems = basketItems;
    }

    public BigDecimal computePrice() {
        BigDecimal totalPrice = new BigDecimal(BigInteger.ZERO);
        for (BasketItem basketItem : basketItems) {
            totalPrice = totalPrice.add(
                    basketItem.getItem().getPrice().multiply(BigDecimal.valueOf(basketItem.getAmount()))
            );
        }

        return totalPrice;
    }

}
