package dev.aziz.grocerystore.handlers;

import dev.aziz.grocerystore.entities.BasketItem;
import dev.aziz.grocerystore.entities.Item;
import dev.aziz.grocerystore.entities.PromotionConfig;
import dev.aziz.grocerystore.entities.User;
import dev.aziz.grocerystore.entities.UserPromotion;
import dev.aziz.grocerystore.enums.PromotionType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PriceHandlerTest {

    @Test
    void oneBasketItem_computePriceTest() {
        //given
        User bob = User.builder().id(1L).login("bob").build();
        Item cola = Item.builder().id(1L).name("Cola").price(new BigDecimal(10)).build();
        BasketItem basketItem1 = BasketItem.builder().id(1L).user(bob).item(cola).amount(10).build();
        PromotionConfig promotionConfig = PromotionConfig.builder()
                .id(1L)
                .promotionType(PromotionType.MORE_FREE)
                .minimumAmount(2)
                .freeAmount(1)
                .item(cola)
                .createdDate(Instant.now())
                .endDate(Instant.now().plus(180, ChronoUnit.DAYS))
                .build();
        List<PromotionConfig> promotions = Arrays.asList(promotionConfig);
        UserPromotion userPromotion = UserPromotion.builder().id(1L).user(bob).promotionConfig(promotionConfig).build();
        List<UserPromotion> userPromotions = List.of(userPromotion);
        PriceHandler priceHandler = new PriceHandler(userPromotions, promotions, List.of(basketItem1));

        //when
        BigDecimal wholeBasketPrice = priceHandler.computePrice();

        //then
        assertAll(() -> {
            assertEquals(BigDecimal.valueOf(50), wholeBasketPrice);
            assertEquals("50", wholeBasketPrice.toString());
        });
    }

    @Test
    void fiveBasketItems_computePriceTest() {
        //given
        User bob = User.builder().id(1L).login("bob").build();
        Item cola = Item.builder().id(1L).name("Cola").price(new BigDecimal(10)).build();
        Item fanta = Item.builder().id(2L).name("Fanta").price(new BigDecimal(20)).build();
        Item blackTea = Item.builder().id(3L).name("Black tea").price(new BigDecimal(30)).build();
        Item greenTea = Item.builder().id(4L).name("Green tea").price(new BigDecimal(40)).build();
        Item iceTea = Item.builder().id(5L).name("Ice tea").price(new BigDecimal(50)).build();
        List<BasketItem> basketItems = Arrays.asList(
                BasketItem.builder().id(1L).amount(10).item(cola).user(bob).build(),     //  100
                BasketItem.builder().id(2L).amount(20).item(fanta).user(bob).build(),    //  400
                BasketItem.builder().id(3L).amount(30).item(blackTea).user(bob).build(), //  900
                BasketItem.builder().id(4L).amount(40).item(greenTea).user(bob).build(), // 1600
                BasketItem.builder().id(5L).amount(50).item(iceTea).user(bob).build()    // 2500
        );
        PromotionConfig promotionConfig = PromotionConfig.builder()
                .id(1L)
                .promotionType(PromotionType.MORE_FREE)
                .minimumAmount(2)
                .freeAmount(1)
                .item(greenTea)
                .createdDate(Instant.now())
                .endDate(Instant.now().plus(180, ChronoUnit.DAYS))
                .build();
        List<PromotionConfig> promotions = Arrays.asList(promotionConfig);
        List<UserPromotion> userPromotions = Arrays.asList(
                UserPromotion.builder().id(1L).user(bob).promotionConfig(promotionConfig).build()
        );

        PriceHandler priceHandler = new PriceHandler(userPromotions, promotions, basketItems);

        //when
        BigDecimal wholeBasketPrice = priceHandler.computePrice();

        //then
        assertAll(() -> {
            assertEquals(BigDecimal.valueOf(4700), wholeBasketPrice);
            assertEquals("4700", wholeBasketPrice.toString());
        });
    }

    @Test
    void tenBasketItems_computePriceTest() {
        //given
        User bob = User.builder().id(1L).login("bob").build();
        Item cola = Item.builder().id(1L).name("Cola").price(new BigDecimal(10)).build();
        Item fanta = Item.builder().id(2L).name("Fanta").price(new BigDecimal(20)).build();
        Item blackTea = Item.builder().id(3L).name("Black tea").price(new BigDecimal(30)).build();
        Item greenTea = Item.builder().id(4L).name("Green tea").price(new BigDecimal(40)).build();
        Item iceTea = Item.builder().id(5L).name("Ice tea").price(new BigDecimal(50)).build();
        Item sevenUp = Item.builder().id(6L).name("Seven Up").price(new BigDecimal(70)).build();
        Item pepsi = Item.builder().id(7L).name("Pepsi").price(new BigDecimal(50)).build();
        Item pepsiZero = Item.builder().id(8L).name("Pepsi Zero").price(new BigDecimal(40)).build();
        Item colaZero = Item.builder().id(9L).name("Cola Zero").price(new BigDecimal(40)).build();
        Item mountainDew = Item.builder().id(10L).name("Mountain Dew").price(new BigDecimal(60)).build();
        List<BasketItem> basketItems = Arrays.asList(
                BasketItem.builder().id(1L).amount(10).item(cola).user(bob).build(),         //  100
                BasketItem.builder().id(2L).amount(20).item(fanta).user(bob).build(),        //  400
                BasketItem.builder().id(3L).amount(30).item(blackTea).user(bob).build(),     //  900
                BasketItem.builder().id(4L).amount(40).item(greenTea).user(bob).build(),     // 1600
                BasketItem.builder().id(5L).amount(50).item(iceTea).user(bob).build(),       // 2500
                BasketItem.builder().id(6L).amount(50).item(sevenUp).user(bob).build(),      // 3500
                BasketItem.builder().id(7L).amount(50).item(pepsi).user(bob).build(),        // 2500
                BasketItem.builder().id(8L).amount(50).item(pepsiZero).user(bob).build(),    // 2000
                BasketItem.builder().id(9L).amount(50).item(colaZero).user(bob).build(),     // 2000
                BasketItem.builder().id(10L).amount(50).item(mountainDew).user(bob).build()  // 3000
        );
        PromotionConfig promotionConfig1 = PromotionConfig.builder()
                .id(1L)
                .promotionType(PromotionType.MORE_FREE)
                .minimumAmount(2)
                .freeAmount(1)
                .item(pepsiZero)
                .createdDate(Instant.now())
                .endDate(Instant.now().plus(180, ChronoUnit.DAYS))
                .build();

        PromotionConfig promotionConfig2 = PromotionConfig.builder()
                .id(2L)
                .promotionType(PromotionType.MORE_FREE)
                .minimumAmount(2)
                .freeAmount(1)
                .item(mountainDew)
                .createdDate(Instant.now())
                .endDate(Instant.now().plus(180, ChronoUnit.DAYS))
                .build();

        List<UserPromotion> userPromotions = List.of(
                UserPromotion.builder().id(1L).user(bob).promotionConfig(promotionConfig1).build(),
                UserPromotion.builder().id(2L).user(bob).promotionConfig(promotionConfig2).build()
        );
        List<PromotionConfig> promotions = List.of(promotionConfig1, promotionConfig2);
        PriceHandler priceHandler = new PriceHandler(userPromotions, promotions, basketItems);

        //when
        BigDecimal wholeBasketPrice = priceHandler.computePrice();

        //then
        assertAll(() -> {
            assertEquals(BigDecimal.valueOf(16000), wholeBasketPrice);
            assertEquals("16000", wholeBasketPrice.toString());
        });
    }
}
