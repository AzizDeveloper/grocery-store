package dev.aziz.grocerystore.handlers;

import dev.aziz.grocerystore.entities.BasketItem;
import dev.aziz.grocerystore.entities.Item;
import dev.aziz.grocerystore.entities.PromotionConfig;
import dev.aziz.grocerystore.entities.User;
import dev.aziz.grocerystore.entities.UserPromotion;
import dev.aziz.grocerystore.enums.PromotionType;
import dev.aziz.grocerystore.repositories.UserPromotionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PriceHandlerTest {

    @Mock
    private UserPromotionRepository userPromotionRepository;

    @Test
    void oneBasketItem_computePriceTest() {
        //given
        User bob = User.builder().id(1L).login("bob").build();
        Item cola = Item.builder().id(1L).name("Cola").price(new BigDecimal(10)).build();
        BasketItem basketItem1 = BasketItem.builder()
                .id(1L)
                .user(bob)
                .item(cola)
                .amount(10)
                .build();
        PromotionConfig promotionConfig = PromotionConfig.builder()
                .id(1L)
                .promotionType(PromotionType.MORE_FREE)
                .minimumAmount(2)
                .freeAmount(1)
                .createdDate(Instant.now())
                .endDate(Instant.now().plus(180, ChronoUnit.DAYS))
                .build();
        UserPromotion userPromotion = UserPromotion.builder()
                .id(1L)
                .user(bob)
                .promotionConfig(promotionConfig)
                .build();
        when(userPromotionRepository.findByUserId(bob.getId())).thenReturn(Optional.ofNullable(userPromotion));
        PriceHandler priceHandler = new PriceHandler(userPromotionRepository, List.of(basketItem1));

        //when
        BigDecimal wholeBasketPrice = priceHandler.computePrice();

        //then
        assertAll(() -> {
            assertEquals(BigDecimal.valueOf(70), wholeBasketPrice);
            assertEquals("70", wholeBasketPrice.toString());
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
                BasketItem.builder().id(1L).amount(10).item(cola).user(bob).build(), // 100 -> 70
                BasketItem.builder().id(2L).amount(20).item(fanta).user(bob).build(), // 400 -> 280
                BasketItem.builder().id(3L).amount(30).item(fanta).user(bob).build(), // 600 -> 400
                BasketItem.builder().id(4L).amount(40).item(blackTea).user(bob).build(), // 1200 -> 810
                BasketItem.builder().id(5L).amount(50).item(blackTea).user(bob).build() // 1500 -> 1020 =2580
        );
        PromotionConfig promotionConfig = PromotionConfig.builder()
                .id(1L)
                .promotionType(PromotionType.MORE_FREE)
                .minimumAmount(2)
                .freeAmount(1)
                .createdDate(Instant.now())
                .endDate(Instant.now().plus(180, ChronoUnit.DAYS))
                .build();
        UserPromotion userPromotion = UserPromotion.builder()
                .id(1L)
                .user(bob)
                .promotionConfig(promotionConfig)
                .build();
        when(userPromotionRepository.findByUserId(bob.getId())).thenReturn(Optional.ofNullable(userPromotion));
        PriceHandler priceHandler = new PriceHandler(userPromotionRepository, basketItems);

        //when
        BigDecimal wholeBasketPrice = priceHandler.computePrice();
        //then
        assertAll(() -> {
            assertEquals(BigDecimal.valueOf(2580), wholeBasketPrice);
            assertEquals("2580", wholeBasketPrice.toString());
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
                BasketItem.builder().id(1L).amount(10).item(cola).user(bob).build(), // 100 -> 70
                BasketItem.builder().id(2L).amount(20).item(cola).user(bob).build(), // 200 -> 140
                BasketItem.builder().id(3L).amount(30).item(cola).user(bob).build(), // 300 -> 200
                BasketItem.builder().id(4L).amount(40).item(fanta).user(bob).build(), // 800 -> 540
                BasketItem.builder().id(5L).amount(50).item(fanta).user(bob).build(), // 1000 -> 680
                BasketItem.builder().id(6L).amount(60).item(fanta).user(bob).build(), // 1200 -> 800
                BasketItem.builder().id(7L).amount(70).item(blackTea).user(bob).build(), // 2100 -> 1410
                BasketItem.builder().id(8L).amount(80).item(blackTea).user(bob).build(), // 2400 -> 1620
                BasketItem.builder().id(9L).amount(90).item(blackTea).user(bob).build(), // 2700 -> 1800
                BasketItem.builder().id(10L).amount(100).item(blackTea).user(bob).build() // 3000 -> 2010
        );
        PromotionConfig promotionConfig = PromotionConfig.builder()
                .id(1L)
                .promotionType(PromotionType.MORE_FREE)
                .minimumAmount(2)
                .freeAmount(1)
                .createdDate(Instant.now())
                .endDate(Instant.now().plus(180, ChronoUnit.DAYS))
                .build();
        UserPromotion userPromotion = UserPromotion.builder()
                .id(1L)
                .user(bob)
                .promotionConfig(promotionConfig)
                .build();
        when(userPromotionRepository.findByUserId(bob.getId())).thenReturn(Optional.ofNullable(userPromotion));
        PriceHandler priceHandler = new PriceHandler(userPromotionRepository, basketItems);

        //when
        BigDecimal wholeBasketPrice = priceHandler.computePrice();

        //then
        assertAll(() -> {
            assertEquals(BigDecimal.valueOf(9270), wholeBasketPrice);
            assertEquals("9270", wholeBasketPrice.toString());
        });
    }
}
