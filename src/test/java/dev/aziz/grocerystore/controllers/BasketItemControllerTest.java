package dev.aziz.grocerystore.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.aziz.grocerystore.dtos.BasketItemDto;
import dev.aziz.grocerystore.dtos.BasketTotalDto;
import dev.aziz.grocerystore.dtos.UserDto;
import dev.aziz.grocerystore.entities.BasketItem;
import dev.aziz.grocerystore.entities.Item;
import dev.aziz.grocerystore.entities.User;
import dev.aziz.grocerystore.services.BasketItemService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BasketItemController.class)
@AutoConfigureMockMvc(addFilters = false)
class BasketItemControllerTest {

    @Autowired
    public MockMvc mockMvc;

    @MockBean
    public BasketItemService basketItemService;

    @Autowired
    public ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        UserDto userDto = UserDto.builder().id(2L).login("bob").build();
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(userDto, null));
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void afterEach() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void addItemToBasketTest() throws Exception{
        //given
        UserDto userDto = UserDto.builder().id(2L).login("bob").build();
        User user = User.builder().id(2L).login("bob").build();
        Item item = Item.builder().id(1L).name("Cola").price(BigDecimal.ONE).build();
        Integer amount = 20;
        BasketItem basketItem = BasketItem.builder().user(user).amount(20).item(item).build();

        //when
        when(basketItemService.addItem(userDto.getLogin(), item.getId(), amount)).thenReturn(basketItem);

        //then
        mockMvc.perform(post("/basket/items/{item_id}/amount/{amount}", item.getId(), amount).principal(new UsernamePasswordAuthenticationToken(userDto, null)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.user.login").value(user.getLogin()))
                .andExpect(jsonPath("$.item.name").value(item.getName()))
                .andExpect(jsonPath("$.amount").value(amount));

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
        mockMvc.perform(get("/basket").principal(new UsernamePasswordAuthenticationToken(userDto, null)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.basketItemDtos[*].name", hasItem("Cola")))
                .andExpect(jsonPath("$.wholeBasketPrice").value(basketTotalDto.getWholeBasketPrice()));
    }

    @Test
    void deleteBasketTest() throws Exception {
        //given
        UserDto userDto = UserDto.builder().id(2L).login("bob").build();
        User user = User.builder().id(2L).login("bob").build();
        Item item1 = Item.builder().id(1L).name("Cola").price(BigDecimal.ONE).build();
        Item item3 = Item.builder().id(3L).name("Black tea").price(BigDecimal.TEN).build();
        BasketItem basketItem1 = BasketItem.builder().user(user).amount(20).item(item3).build();
        BasketItem basketItem2 = BasketItem.builder().user(user).amount(30).item(item3).build();
        BasketItem basketItem3 = BasketItem.builder().user(user).amount(30).item(item1).build();
        List<BasketItem> basketItemList = Arrays.asList(basketItem1, basketItem2, basketItem3);

        //when
        when(basketItemService.deleteBasket(user.getId())).thenReturn(basketItemList);

        //then
        mockMvc.perform(delete("/basket").principal(new UsernamePasswordAuthenticationToken(userDto, null)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(basketItemList.size()))
                .andExpect(jsonPath("$.[2].item.name").value(item1.getName()))
                .andExpect(jsonPath("$.[*].item.name", hasItem(item3.getName())));
    }
}
