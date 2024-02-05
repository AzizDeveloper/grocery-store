package dev.aziz.grocerystore.handlers;

import dev.aziz.grocerystore.entities.BasketItem;
import dev.aziz.grocerystore.entities.ItemPromotion;
import dev.aziz.grocerystore.entities.UserPromotion;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Slf4j
public class PriceHandler {
    private final List<ItemPromotion> itemPromotions;

    private final List<UserPromotion> userPromotions;

    private final List<BasketItem> basketItems;

    public PriceHandler(List<ItemPromotion> itemPromotions,
                        List<UserPromotion> userPromotions,
                        List<BasketItem> basketItems) {
        this.itemPromotions = itemPromotions;
        this.userPromotions = userPromotions;
        this.basketItems = basketItems;
    }

    public BigDecimal computePrice() {
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (BasketItem item : basketItems) {
            if (hasItemPromotion(item) && !userPromotions.isEmpty()) {
                log.info("hasItemPromotion(item) && !userPromotions.isEmpty() condition passed.");
                UserPromotion userPromotion = findPromotionForUser(item.getAmount());
                ItemPromotion itemPromotion = findPromotionForItem(item);
                if (((item.getAmount() / userPromotion.getPromotionConfig().getMinimumAmount()) * userPromotion.getPromotionConfig().getFreeAmount())
                        >=
                        ((item.getAmount() / itemPromotion.getPromotionConfig().getMinimumAmount()) * itemPromotion.getPromotionConfig().getFreeAmount())) {
                    totalPrice = totalPrice.add(computePriceWithUserPromotion(item, userPromotion));
                } else {
                    totalPrice = totalPrice.add(computePriceWithItemPromotion(item, itemPromotion));
                }

            } else if (!userPromotions.isEmpty()) {
                log.info("!userPromotions.isEmpty() condition passed.");
                UserPromotion userPromotion = findPromotionForUser(item.getAmount());
                totalPrice = totalPrice.add(computePriceWithUserPromotion(item, userPromotion));
            } else if ((hasItemPromotion(item))) {
                log.info("hasItemPromotion(item) condition passed.");
                ItemPromotion itemPromotion = findPromotionForItem(item);
                totalPrice = totalPrice.add(computePriceWithItemPromotion(item, itemPromotion));
            } else {
                BigDecimal add = item.getItem().getPrice().multiply(BigDecimal.valueOf(item.getAmount()));
                log.info("Total items without promotion: {}, and total price: {}", item.getAmount(), add);
                totalPrice = totalPrice.add(add);
            }
        }
        return totalPrice;
    }

    private UserPromotion findPromotionForUser(Integer itemAmount) {
        if (userPromotions.isEmpty()) {
            return new UserPromotion();
        }
        if (userPromotions.size() == 1) {
            return userPromotions.get(0);
        }
        UserPromotion bestPromotion = null;
        int maxFreeItems = 0;

        for (UserPromotion promotion : userPromotions) {
            int eligibleSets = itemAmount / promotion.getPromotionConfig().getMinimumAmount();
            int totalFreeItems = eligibleSets * promotion.getPromotionConfig().getFreeAmount();

            if (totalFreeItems > maxFreeItems) {
                maxFreeItems = totalFreeItems;
                bestPromotion = promotion;
            }
        }
        return bestPromotion;
    }

    private ItemPromotion findPromotionForItem(BasketItem item) {
        if (itemPromotions.isEmpty()) {
            return new ItemPromotion();
        }
        if (itemPromotions.size() == 1) {
            return itemPromotions.get(0);
        }
        ItemPromotion bestPromotion = null;
        int maxFreeItems = 0;

        for (ItemPromotion promotion : itemPromotions) {
            int eligibleSets = item.getAmount() / promotion.getPromotionConfig().getMinimumAmount();
            int totalFreeItems = eligibleSets * promotion.getPromotionConfig().getFreeAmount();

            if (totalFreeItems > maxFreeItems) {
                maxFreeItems = totalFreeItems;
                bestPromotion = promotion;
            }
        }
        return bestPromotion;
    }

    private BigDecimal computePriceWithUserPromotion(BasketItem basketItem, UserPromotion userPromotion) {
        Integer minAmount = userPromotion.getPromotionConfig().getMinimumAmount();
        Integer freeAmount = userPromotion.getPromotionConfig().getFreeAmount();
        Instant date = userPromotion.getPromotionConfig().getEndDate();

        int value = Instant.now().compareTo(date);
        int free = 0;

        if (value < 0) {
            if (basketItem.getAmount() >= minAmount) {
                free = (basketItem.getAmount() / minAmount) * freeAmount;
            }
            int paidItems = basketItem.getAmount() - free;
            log.info("Free items computed by UserPromotion: {}", free);
            log.info("Total number of items: {}", basketItem.getAmount());
            return basketItem.getItem().getPrice().multiply(BigDecimal.valueOf(paidItems));
        } else {
            return basketItem.getItem().getPrice().multiply(BigDecimal.valueOf(basketItem.getAmount()));
        }
    }

    private BigDecimal computePriceWithItemPromotion(BasketItem basketItem, ItemPromotion itemPromotion) {
        Integer minAmount = itemPromotion.getPromotionConfig().getMinimumAmount();
        Integer freeAmount = itemPromotion.getPromotionConfig().getFreeAmount();
        Instant date = itemPromotion.getPromotionConfig().getEndDate();

        int value = Instant.now().compareTo(date);
        int free = 0;

        if (value < 0) {
            if (basketItem.getAmount() >= minAmount) {
                free = (basketItem.getAmount() / minAmount) * freeAmount;
            }
            int paidItems = basketItem.getAmount() - free;
            log.info("Free items computed by UserPromotion: {}", free);
            log.info("Total number of items: {}", basketItem.getAmount());
            return basketItem.getItem().getPrice().multiply(BigDecimal.valueOf(paidItems));
        } else {
            return basketItem.getItem().getPrice().multiply(BigDecimal.valueOf(basketItem.getAmount()));
        }
    }

    public boolean hasItemPromotion(BasketItem basketItem) {
        for (ItemPromotion itemPromotion : itemPromotions) {
            if (basketItem.getItem().getId() == itemPromotion.getItem().getId()) {
                return true;
            }
        }
        return false;
    }

}
