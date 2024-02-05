package dev.aziz.grocerystore.handlers;

import dev.aziz.grocerystore.entities.BasketItem;
import dev.aziz.grocerystore.entities.Item;
import dev.aziz.grocerystore.entities.ItemPromotion;
import dev.aziz.grocerystore.entities.PromotionConfig;
import dev.aziz.grocerystore.entities.User;
import dev.aziz.grocerystore.entities.UserPromotion;
import dev.aziz.grocerystore.enums.PromotionType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
                .createdDate(Instant.now())
                .endDate(Instant.now().plus(180, ChronoUnit.DAYS))
                .build();
        UserPromotion userPromotion = UserPromotion.builder().id(1L).user(bob).promotionConfig(promotionConfig).build();
        List<UserPromotion> userPromotions = List.of(userPromotion);
        ItemPromotion itemPromotion = ItemPromotion.builder().id(1L).item(cola).promotionConfig(promotionConfig).build();
        List<ItemPromotion> itemPromotions = List.of(itemPromotion);
        PriceHandler priceHandler = new PriceHandler(itemPromotions, userPromotions, List.of(basketItem1));

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
        List<BasketItem> basketItems = Arrays.asList(
                BasketItem.builder().id(1L).amount(10).item(cola).user(bob).build(),     //  100
                BasketItem.builder().id(2L).amount(20).item(fanta).user(bob).build(),    //  400
                BasketItem.builder().id(3L).amount(30).item(fanta).user(bob).build(),    //  600
                BasketItem.builder().id(4L).amount(40).item(blackTea).user(bob).build(), // 1200
                BasketItem.builder().id(5L).amount(50).item(blackTea).user(bob).build()  // 1500
        );
        PromotionConfig promotionConfig = PromotionConfig.builder()
                .id(1L)
                .promotionType(PromotionType.MORE_FREE)
                .minimumAmount(2)
                .freeAmount(1)
                .createdDate(Instant.now())
                .endDate(Instant.now().plus(180, ChronoUnit.DAYS))
                .build();
        List<UserPromotion> userPromotions = new ArrayList<>();
        ItemPromotion itemPromotion1 = ItemPromotion.builder().id(1L).item(cola).promotionConfig(promotionConfig).build();
        ItemPromotion itemPromotion2 = ItemPromotion.builder().id(2L).item(fanta).promotionConfig(promotionConfig).build();

        List<ItemPromotion> itemPromotions = List.of(itemPromotion1, itemPromotion2);
        PriceHandler priceHandler = new PriceHandler(itemPromotions, userPromotions, basketItems);

        //when
        BigDecimal wholeBasketPrice = priceHandler.computePrice();

        //then
        assertAll(() -> {
            assertEquals(BigDecimal.valueOf(3250), wholeBasketPrice);
            assertEquals("3250", wholeBasketPrice.toString());
        });
    }

    @Test
    void tenBasketItems_computePriceTest() {
        //given
        User bob = User.builder().id(1L).login("bob").build();
        Item cola = Item.builder().id(1L).name("Cola").price(new BigDecimal(10)).build();
        Item fanta = Item.builder().id(2L).name("Fanta").price(new BigDecimal(20)).build();
        Item blackTea = Item.builder().id(3L).name("Black tea").price(new BigDecimal(30)).build();
        List<BasketItem> basketItems = Arrays.asList(
                BasketItem.builder().id(1L).amount(10).item(cola).user(bob).build(),      //  100
                BasketItem.builder().id(2L).amount(20).item(cola).user(bob).build(),      //  200
                BasketItem.builder().id(3L).amount(30).item(cola).user(bob).build(),      //  300
                BasketItem.builder().id(4L).amount(40).item(fanta).user(bob).build(),     //  800
                BasketItem.builder().id(5L).amount(50).item(fanta).user(bob).build(),     // 1000
                BasketItem.builder().id(6L).amount(60).item(fanta).user(bob).build(),     // 1200
                BasketItem.builder().id(7L).amount(70).item(blackTea).user(bob).build(),  // 2100
                BasketItem.builder().id(8L).amount(80).item(blackTea).user(bob).build(),  // 2400
                BasketItem.builder().id(9L).amount(90).item(blackTea).user(bob).build(),  // 2700
                BasketItem.builder().id(10L).amount(100).item(blackTea).user(bob).build() // 3000
        );
        PromotionConfig promotionConfig = PromotionConfig.builder()
                .id(1L)
                .promotionType(PromotionType.MORE_FREE)
                .minimumAmount(2)
                .freeAmount(1)
                .createdDate(Instant.now())
                .endDate(Instant.now().plus(180, ChronoUnit.DAYS))
                .build();
        UserPromotion userPromotion = UserPromotion.builder().id(1L).user(bob).promotionConfig(promotionConfig).build();
        List<UserPromotion> userPromotions = List.of(userPromotion);
        List<ItemPromotion> itemPromotions = new ArrayList<>();
        PriceHandler priceHandler = new PriceHandler(itemPromotions, userPromotions, basketItems);

        //when
        BigDecimal wholeBasketPrice = priceHandler.computePrice();

        //then
        assertAll(() -> {
            assertEquals(BigDecimal.valueOf(6900), wholeBasketPrice);
            assertEquals("6900", wholeBasketPrice.toString());
        });
    }
}
