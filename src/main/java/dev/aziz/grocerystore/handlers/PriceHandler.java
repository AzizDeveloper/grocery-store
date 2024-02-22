package dev.aziz.grocerystore.handlers;

import dev.aziz.grocerystore.entities.BasketItem;
import dev.aziz.grocerystore.entities.PromotionConfig;
import dev.aziz.grocerystore.entities.UserPromotion;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Slf4j
public class PriceHandler {

    private final List<UserPromotion> userPromotions;

    private final List<PromotionConfig> promotionConfigs;

    private final List<BasketItem> basketItems;

    public PriceHandler(List<UserPromotion> userPromotions,
                        List<PromotionConfig> promotionConfigs,
                        List<BasketItem> basketItems) {
        this.userPromotions = userPromotions;
        this.promotionConfigs = promotionConfigs;
        this.basketItems = basketItems;
    }

    public BigDecimal computePrice() {
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (BasketItem item : basketItems) {
            if (hasItemPromotion(item)) {
                log.info("hasItemPromotion(item) condition passed for this item: {}.", item.getItem().getName());

                PromotionConfig itemPromotion = null;

                for (UserPromotion promotion : userPromotions) {
                    if (item.getId() == promotion.getPromotionConfig().getItem().getId()) {
                        itemPromotion = promotion.getPromotionConfig();
                    }
                }
                if (itemPromotion != null) {
                    totalPrice = totalPrice.add(computePriceWithPromotion(item, itemPromotion));
                } else {
                    BigDecimal add = item.getItem().getPrice().multiply(BigDecimal.valueOf(item.getAmount()));
                    log.info("Total items without promotion: {}, and total price: {}", item.getAmount(), add);
                    totalPrice = totalPrice.add(add);
                }
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
        Instant date = promotionConfig.getEndDate();
        int value = 0;

        Instant now = Instant.now();
        value = now.compareTo(date);
        int free = 0;

        if (value < 0) {
            if (basketItem.getAmount() >= minAmount) {
                free = (basketItem.getAmount() / minAmount) * freeAmount;
            }
            int paidItems = basketItem.getAmount() - free;
            log.info("Free items computed by Promotion: {}", free);
            log.info("Total number of items: {}", basketItem.getAmount());
            return basketItem.getItem().getPrice().multiply(BigDecimal.valueOf(paidItems));
        } else {
            return basketItem.getItem().getPrice().multiply(BigDecimal.valueOf(basketItem.getAmount()));
        }
    }

    public boolean hasItemPromotion(BasketItem basketItem) {
        for (PromotionConfig promotionConfig : promotionConfigs) {
            if (basketItem.getItem().getId() == promotionConfig.getItem().getId()) {
                return true;
            }
        }
        return false;
    }

}
