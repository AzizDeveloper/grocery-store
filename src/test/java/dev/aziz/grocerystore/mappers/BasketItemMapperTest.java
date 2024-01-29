package dev.aziz.grocerystore.mappers;

import dev.aziz.grocerystore.dtos.BasketItemDto;
import dev.aziz.grocerystore.entities.BasketItem;
import dev.aziz.grocerystore.entities.Item;
import dev.aziz.grocerystore.entities.User;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BasketItemMapperTest {

    @Spy
    private BasketItemMapper basketItemMapper = new BasketItemMapperImpl();

    @Test
    void basketItemToBasketItemDtoTest() {
        //given
        User user = User.builder().id(1L).login("azizdev").firstName("Aziz").lastName("Abdukarimov").password("12345678").build();
        Item item = Item.builder().id(1L).name("Cola").description("Sparkling soda with black color").price(BigDecimal.ONE).stockAmount(10).build();
        BasketItem basketItem = BasketItem.builder()
                .id(1L)
                .user(user)
                .item(item)
                .amount(11)
                .build();

        //when
        BasketItemDto basketItemDto = basketItemMapper.basketItemToBasketItemDto(basketItem);

        //then
        assertAll(() -> {
            assertEquals(basketItem.getItem().getName(), basketItemDto.getName());
            assertEquals(basketItem.getItem().getPrice().toString(), basketItemDto.getUnitPrice());
            assertEquals( (basketItem.getItem().getPrice().multiply(BigDecimal.valueOf(basketItem.getAmount())).toString() ), basketItemDto.getTotalPrice());
            assertEquals(basketItem.getAmount(), basketItemDto.getStockAmount());
        });
    }

}
