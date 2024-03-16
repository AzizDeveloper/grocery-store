package dev.aziz.grocerystore.handlers;

import dev.aziz.grocerystore.entities.BasketItem;
import dev.aziz.grocerystore.entities.PromotionConfig;
import dev.aziz.grocerystore.entities.UserPromotion;
import dev.aziz.grocerystore.enums.PromotionType;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
public class PriceHandler {

    private final PriceHandlerContext priceHandlerContext;

    public PriceHandler(PriceHandlerContext priceHandlerContext) {
        this.priceHandlerContext = priceHandlerContext;
    }

    public BigDecimal computePrice() {
        BigDecimal totalPrice = BigDecimal.ZERO;
        List<BasketItem> basketItems = priceHandlerContext.getBasketItems();
        List<UserPromotion> userPromotions = priceHandlerContext.getUserPromotions();
        for (BasketItem basketItem : basketItems) {
            PromotionConfig itemPromotion = null;

            for (UserPromotion promotion : userPromotions) {
                if (basketItem.getItem().getId() == promotion.getPromotionConfig().getItem().getId()) {
                    itemPromotion = promotion.getPromotionConfig();
                }
            }
            if (itemPromotion != null) {
                log.info("itemPromotion != null condition has passed for this item: {}.", basketItem.getItem().getName());
                BigDecimal computedPriceWithPromotion = computePriceWithPromotion(basketItem, itemPromotion);
                BigDecimal divided = computedPriceWithPromotion.divide(BigDecimal.valueOf(100));
                log.info("Divided price: {}", divided);
                totalPrice = totalPrice.add(divided);
            } else {
                BigDecimal add = basketItem.getItem().getPrice().multiply(BigDecimal.valueOf(basketItem.getAmount()));
                log.info("Total items without promotion: {}, and total price: {}", basketItem.getAmount(), add);
                log.info("Item name {}", basketItem.getItem().getName());
                totalPrice = totalPrice.add(add);
            }
        }
        log.info("Total price of the whole basket: {}", totalPrice);
        return totalPrice;
    }

    private BigDecimal computePriceWithPromotion(BasketItem basketItem, PromotionConfig promotionConfig) {
        Integer minAmount = promotionConfig.getMinimumAmount();
        Integer freeAmount = promotionConfig.getFreeAmount();
        Integer free = 0;

        if (basketItem.getAmount() >= minAmount) {
            if (PromotionType.MORE_FREE.equals(promotionConfig.getPromotionType())) {
                free = ( (basketItem.getAmount() * 100) / ( (minAmount + 1) * 100) ) * freeAmount;
            } else if (PromotionType.MORE_OFF.equals(promotionConfig.getPromotionType())) {
                free = ( ( basketItem.getAmount() * 100 ) /( ( minAmount + 1 ) * 100) ) * freeAmount;
            }
        }

        Integer paidItems = (basketItem.getAmount() * 100) - free;
        log.info("paidItems : {}", paidItems);
        log.info("Free items computed by Promotion: {}", free);
        log.info("Total number of items: {}", basketItem.getAmount());
        return basketItem.getItem().getPrice().multiply(BigDecimal.valueOf(paidItems));
    }

}
