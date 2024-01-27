package dev.aziz.grocerystore.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.aziz.grocerystore.dtos.BasketItemDto;
import dev.aziz.grocerystore.dtos.BasketTotalDto;
import dev.aziz.grocerystore.dtos.UserDto;
import dev.aziz.grocerystore.entities.BasketItem;
import dev.aziz.grocerystore.entities.Item;
import dev.aziz.grocerystore.entities.User;
import dev.aziz.grocerystore.services.BasketItemService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(BasketItemController.class)
class BasketItemControllerTest {

    @Autowired
    public MockMvc mockMvc;

    @MockBean
    public BasketItemService basketItemService;

    @Autowired
    public ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        UserDto userDto = UserDto.builder().id(2L).login("bob").build();
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(userDto, null));
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void addItemToBasketTest() throws Exception {
        //given
        UserDto userDto = UserDto.builder().id(2L).login("bob").build();
        Item item = Item.builder().id(1L).name("Cola").price(BigDecimal.ONE).build();
        Integer amount = 20;
        BasketItemDto basketItemDto = BasketItemDto.builder()
                .name("Cola")
                .unitPrice("1")
                .stockAmount(20)
                .build();

        //when
        when(basketItemService.addItem(userDto.getLogin(), item.getId(), amount)).thenReturn(basketItemDto);

        //then
        mockMvc.perform(post("/basket/items/{item_id}/amount/{amount}", item.getId(), amount))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(item.getName()))
                .andExpect(jsonPath("$.stockAmount").value(amount));
    }

    @Test
    void getBasketTest() throws Exception {
        //given
        UserDto userDto = UserDto.builder().id(2L).login("bob").build();
        List<BasketItemDto> basketItemDtos = Arrays.asList(
                BasketItemDto.builder().name("Cola").stockAmount(30).unitPrice("1").totalPrice("30").build(),
                BasketItemDto.builder().name("Black tea").stockAmount(50).unitPrice("10").totalPrice("500").build()
        );
        BasketTotalDto basketTotalDto = BasketTotalDto.builder()
                .wholeBasketPrice("530")
                .basketItemDtos(basketItemDtos)
                .build();

        //when
        when(basketItemService.getItems(userDto.getLogin())).thenReturn(basketTotalDto);

        //then
        mockMvc.perform(get("/basket"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.wholeBasketPrice").value("530"))
                .andExpect(jsonPath("$.basketItemDtos[*].name", hasItem("Cola")))
                .andExpect(jsonPath("$.wholeBasketPrice").value(basketTotalDto.getWholeBasketPrice()));
    }

    @Test
    void deleteBasketTest() throws Exception {
        //given
        User user = User.builder().id(2L).login("bob").build();
        Item item1 = Item.builder().id(1L).name("Cola").price(BigDecimal.ONE).build();
        Item item3 = Item.builder().id(3L).name("Black tea").price(BigDecimal.TEN).build();
        BasketItemDto basketItem1 = BasketItemDto.builder().stockAmount(20).name(item3.getName()).build();
        BasketItemDto basketItem2 = BasketItemDto.builder().stockAmount(30).name(item3.getName()).build();
        BasketItemDto basketItem3 = BasketItemDto.builder().stockAmount(30).name(item1.getName()).build();
        List<BasketItemDto> basketItemList = Arrays.asList(basketItem1, basketItem2, basketItem3);

        //when
        when(basketItemService.deleteBasket(user.getId())).thenReturn(basketItemList);

        //then
        mockMvc.perform(delete("/basket"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(basketItemList.size()))
                .andExpect(jsonPath("$.[2].name").value(item1.getName()))
                .andExpect(jsonPath("$.[*].name", hasItem(item3.getName())));
    }
}
