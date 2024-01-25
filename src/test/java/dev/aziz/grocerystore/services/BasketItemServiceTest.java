package dev.aziz.grocerystore.services;

import dev.aziz.grocerystore.dtos.BasketItemDto;
import dev.aziz.grocerystore.dtos.BasketTotalDto;
import dev.aziz.grocerystore.entities.BasketItem;
import dev.aziz.grocerystore.entities.Item;
import dev.aziz.grocerystore.entities.User;
import dev.aziz.grocerystore.repositories.BasketItemRepository;
import dev.aziz.grocerystore.repositories.ItemRepository;
import dev.aziz.grocerystore.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasketItemServiceTest {

    @InjectMocks
    private BasketItemService basketItemService;

    @Mock
    private BasketItemRepository basketItemRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    void addItemTest() {
        //given
        String login = "azizdev";
        Long itemId = 4L;
        Integer amount = 30;
        User user = User.builder().login("azidev").build();
        Item item = Item.builder().id(4L).name("Wine").build();

        //when
        BasketItem build = BasketItem.builder()
                .user(user)
                .item(item)
                .amount(amount)
                .build();
        when(userRepository.findByLogin(login)).thenReturn(Optional.ofNullable(user));
        when(itemRepository.findItemById(itemId)).thenReturn(Optional.ofNullable(item));
        when(basketItemRepository.save(build)).thenReturn(build);

        BasketItem basketItem = basketItemService.addItem(login, itemId, amount);

        //then
        assertAll(() -> {
            assertEquals(basketItem.getItem().getName(), item.getName());
            assertEquals(basketItem.getUser().getLogin(), user.getLogin());
            assertEquals(basketItem.getAmount(), amount);
        });
    }

    @Test
    void getItemsTest() {
        //given
        Long userId = 2L;
        User user = User.builder().id(2L).login("bob").build();
        Item item1 = Item.builder().id(1L).name("Cola").price(BigDecimal.ONE).build();
        Item item3 = Item.builder().id(3L).name("Black tea").price(BigDecimal.TEN).build();
        BasketItem basketItem1 = BasketItem.builder().amount(20).item(item3).build();
        BasketItem basketItem2 = BasketItem.builder().amount(30).item(item3).build();
        BasketItem basketItem3 = BasketItem.builder().amount(30).item(item1).build();
        List<BasketItem> basketItemList = Arrays.asList(basketItem1, basketItem2, basketItem3);

        List<BasketItemDto> basketItemDtos = Arrays.asList(
                BasketItemDto.builder().name("Cola").stockAmount(30).unitPrice("1").totalPrice("30").build(),
                BasketItemDto.builder().name("Black tea").stockAmount(50).unitPrice("10").totalPrice("500").build()
        );
        BasketTotalDto basketTotalDto = BasketTotalDto.builder()
                .wholeBasketPrice("530")
                .basketItemDtos(basketItemDtos)
                .build();

        //when
        when(userRepository.findByLogin(user.getLogin())).thenReturn(Optional.of(user));
        when(basketItemRepository.findBasketsByUserId(userId)).thenReturn(Optional.of(basketItemList));
        BasketTotalDto result = basketItemService.getItems(user.getLogin());

        //then
        assertAll(() -> {
            assertEquals(result.getWholeBasketPrice(), basketTotalDto.getWholeBasketPrice());
            assertEquals(result, basketTotalDto);
        });
    }

    @Test
    void deleteBasketTest() {
        //given
        Long userId = 2L;
        User user = User.builder().id(2L).login("bob").build();
        Item item1 = Item.builder().id(1L).build();
        Item item3 = Item.builder().id(3L).build();
        BasketItem basketItem1 = BasketItem.builder().amount(20).item(item3).build();
        BasketItem basketItem2 = BasketItem.builder().amount(30).item(item3).build();
        BasketItem basketItem3 = BasketItem.builder().amount(30).item(item1).build();
        List<BasketItem> basketItemList = Arrays.asList(basketItem1, basketItem2, basketItem3);

        //when
        when(basketItemRepository.findBasketsByUserId(userId)).thenReturn(Optional.of(basketItemList));
        List<BasketItem> result = basketItemService.deleteBasket(userId);

        //then
        verify(basketItemRepository, times(1)).deleteBasketItemsByUserId(userId);
        assertEquals(basketItemList, result);
    }
}
