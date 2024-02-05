package dev.aziz.grocerystore.handlers;

import dev.aziz.grocerystore.entities.BasketItem;
import dev.aziz.grocerystore.entities.UserPromotion;
import dev.aziz.grocerystore.exceptions.AppException;
import dev.aziz.grocerystore.repositories.UserPromotionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.List;

@Slf4j
public class PriceHandler {

    private final UserPromotionRepository userPromotionRepository;

    private List<BasketItem> basketItems;

    public PriceHandler(UserPromotionRepository userPromotionRepository, List<BasketItem> basketItems) {
        this.userPromotionRepository = userPromotionRepository;
        this.basketItems = basketItems;
    }

    public BigDecimal computePrice() {
        Long id = basketItems.get(0).getUser().getId();
        UserPromotion userPromotion = userPromotionRepository.findByUserId(id)
                .orElseThrow(() -> new AppException("User promotion not found by id:" + id, HttpStatus.NOT_FOUND));
        Integer minAmount = userPromotion.getPromotionConfig().getMinimumAmount();
        Integer freeAmount = userPromotion.getPromotionConfig().getFreeAmount();
        Instant date = userPromotion.getPromotionConfig().getEndDate();

        int value = Instant.now().compareTo(date);
        int total = minAmount + freeAmount;
        int free = 0;

        BigDecimal totalPrice = new BigDecimal(BigInteger.ZERO);

        if (value < 0) {
            for (BasketItem basketItem : basketItems) {
                if (basketItem.getAmount() >= total) {
                    free = basketItem.getAmount() / total;
                }

                int afterReducingFreeItems = basketItem.getAmount() - free;
                totalPrice = totalPrice.add(
                        basketItem.getItem().getPrice().multiply(BigDecimal.valueOf(afterReducingFreeItems))
                );
            }
        }

        log.info("Whole basket price: {}", totalPrice);
        return totalPrice;
    }

}
