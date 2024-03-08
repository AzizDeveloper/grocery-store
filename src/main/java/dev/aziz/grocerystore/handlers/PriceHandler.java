package dev.aziz.grocerystore.handlers;

import dev.aziz.grocerystore.entities.BasketItem;
import dev.aziz.grocerystore.entities.PromotionConfig;
import dev.aziz.grocerystore.entities.UserPromotion;
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

        for (BasketItem item : basketItems) {
            PromotionConfig itemPromotion = null;

            for (UserPromotion promotion : userPromotions) {
                if (item.getId() == promotion.getPromotionConfig().getItem().getId()) {
                    itemPromotion = promotion.getPromotionConfig();
                }
            }
            if (itemPromotion != null) {
                log.info("itemPromotion != null condition has passed for this item: {}.", item.getItem().getName());
                totalPrice = totalPrice.add(computePriceWithPromotion(item, itemPromotion));
            } else {
                BigDecimal add = item.getItem().getPrice().multiply(BigDecimal.valueOf(item.getAmount()));
                log.info("Total items without promotion: {}, and total price: {}", item.getAmount(), add);
                totalPrice = totalPrice.add(add);
            }
        }
        return totalPrice;
    }

    private BigDecimal computePriceWithPromotion(BasketItem basketItem, PromotionConfig promotionConfig) {
        Integer minAmount = promotionConfig.getMinimumAmount();
        Integer freeAmount = promotionConfig.getFreeAmount();
        int free = 0;

        if (basketItem.getAmount() >= minAmount) {
            free = ( basketItem.getAmount() / (minAmount + freeAmount) ) * freeAmount;
        }
        int paidItems = basketItem.getAmount() - free;
        log.info("Free items computed by Promotion: {}", free);
        log.info("Total number of items: {}", basketItem.getAmount());
        return basketItem.getItem().getPrice().multiply(BigDecimal.valueOf(paidItems));
    }

}
