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
                totalPrice = totalPrice.add(computePriceWithPromotion(basketItem, itemPromotion));
            } else {
                BigDecimal add = basketItem.getItem().getPrice().multiply(BigDecimal.valueOf(basketItem.getAmount()));
                log.info("Total items without promotion: {}, and total price: {}", basketItem.getAmount(), add);
                log.info("Item name {}", basketItem.getItem().getName());
                totalPrice = totalPrice.add(add);
            }
        }
        return totalPrice;
    }

    private BigDecimal computePriceWithPromotion(BasketItem basketItem, PromotionConfig promotionConfig) {
        Integer minAmount = promotionConfig.getMinimumAmount();
        Double freeAmount = promotionConfig.getFreeAmount();
        Double free = 0.0;

        if (basketItem.getAmount() >= minAmount) {
            if (PromotionType.MORE_FREE.equals(promotionConfig.getPromotionType())) {
                int intFreeAmount = freeAmount.intValue();
                free = ( basketItem.getAmount() / (minAmount + intFreeAmount) ) * freeAmount;
            } else if (PromotionType.MORE_OFF.equals(promotionConfig.getPromotionType())) {
                if (promotionConfig.getFreeAmount() < 1) {
                    free = (basketItem.getAmount() / (minAmount + 1)) * freeAmount;
                } else {
                    free = ( basketItem.getAmount() / (minAmount + freeAmount) ) * freeAmount;
                }
            }
        }

        Double paidItems = basketItem.getAmount() - free;
        log.info("paidItems which is Double: {}", paidItems);
        log.info("Free items computed by Promotion: {}", free);
        log.info("Total number of items: {}", basketItem.getAmount());
        return basketItem.getItem().getPrice().multiply(BigDecimal.valueOf(paidItems));
    }

}
