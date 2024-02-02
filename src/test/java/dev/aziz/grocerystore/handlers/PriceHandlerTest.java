package dev.aziz.grocerystore.handlers;

import dev.aziz.grocerystore.entities.BasketItem;
import dev.aziz.grocerystore.entities.Item;
import dev.aziz.grocerystore.entities.User;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
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
        BasketItem basketItem1 = BasketItem.builder()
                .id(1L)
                .amount(10)
                .item(cola)
                .user(bob)
                .build();
        PriceHandler priceHandler = new PriceHandler(List.of(basketItem1));

        //when
        BigDecimal wholeBasketPrice = priceHandler.computePrice();

        //then
        assertAll(() -> {
            assertEquals(BigDecimal.valueOf(100), wholeBasketPrice);
            assertEquals("100", wholeBasketPrice.toString());
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
                BasketItem.builder().id(1L).amount(10).item(cola).user(bob).build(), // 100
                BasketItem.builder().id(2L).amount(20).item(fanta).user(bob).build(), // 400
                BasketItem.builder().id(3L).amount(30).item(fanta).user(bob).build(), // 600
                BasketItem.builder().id(4L).amount(40).item(blackTea).user(bob).build(), // 1200
                BasketItem.builder().id(5L).amount(50).item(blackTea).user(bob).build() // 1500
        );
        PriceHandler priceHandler = new PriceHandler(basketItems);

        //when
        BigDecimal wholeBasketPrice = priceHandler.computePrice();
        //then
        assertAll(() -> {
            assertEquals(BigDecimal.valueOf(3800), wholeBasketPrice);
            assertEquals("3800", wholeBasketPrice.toString());
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
                BasketItem.builder().id(1L).amount(10).item(cola).user(bob).build(), // 100
                BasketItem.builder().id(2L).amount(20).item(cola).user(bob).build(), // 200
                BasketItem.builder().id(3L).amount(30).item(cola).user(bob).build(), // 300
                BasketItem.builder().id(4L).amount(40).item(fanta).user(bob).build(), // 800
                BasketItem.builder().id(5L).amount(50).item(fanta).user(bob).build(), // 1000
                BasketItem.builder().id(6L).amount(60).item(fanta).user(bob).build(), // 1200
                BasketItem.builder().id(7L).amount(70).item(blackTea).user(bob).build(), // 2100
                BasketItem.builder().id(8L).amount(80).item(blackTea).user(bob).build(), // 2400
                BasketItem.builder().id(9L).amount(90).item(blackTea).user(bob).build(), // 2700
                BasketItem.builder().id(10L).amount(100).item(blackTea).user(bob).build() // 3000
        );
        PriceHandler priceHandler = new PriceHandler(basketItems);

        //when
        BigDecimal wholeBasketPrice = priceHandler.computePrice();

        //then
        assertAll(() -> {
            assertEquals(BigDecimal.valueOf(13800), wholeBasketPrice);
            assertEquals("13800", wholeBasketPrice.toString());
        });
    }
}
