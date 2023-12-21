package dev.aziz.grocerystore.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.aziz.grocerystore.dtos.ItemDto;
import dev.aziz.grocerystore.dtos.ItemSummaryDto;
import dev.aziz.grocerystore.entities.Category;
import dev.aziz.grocerystore.entities.Item;
import dev.aziz.grocerystore.repositories.ItemRepository;
import dev.aziz.grocerystore.services.ItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    public MockMvc mockMvc;

    @MockBean
    public ItemService itemService;

    @Autowired
    public ObjectMapper objectMapper;

    @Test
    void getAllItemsTest() throws Exception {
        //given
        List<ItemSummaryDto> itemSummaryDtos = new ArrayList<>();
        Category drinks = new Category(1L, "Drinks", null);
        Category softs = new Category(2L, "Softs", drinks);
        Category soda = new Category(3L, "Soda", softs);
        Category tea = new Category(4L, "Tea", softs);
        Category alcohol = new Category(5L, "Alcohol", drinks);
        itemSummaryDtos.add(new ItemSummaryDto(1L, "Cola", "0.500", soda));
        itemSummaryDtos.add(new ItemSummaryDto(2L, "Fanta", "0.400", soda));
        itemSummaryDtos.add(new ItemSummaryDto(3L, "Black tea", "0.300", tea));
        itemSummaryDtos.add(new ItemSummaryDto(4L, "Wine", "1.000", alcohol));


        //when
        when(itemService.getItemSummaries()).thenReturn(itemSummaryDtos);

        //then
        mockMvc.perform(get("/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(itemSummaryDtos.get(0).getName()))
                .andExpect(jsonPath("$[3].name").value(itemSummaryDtos.get(3).getName()))
                .andExpect(jsonPath("$[2].price").value(itemSummaryDtos.get(2).getPrice()));

    }


    @Test
    void getOneItemTest() throws Exception {
        //given
        Category drinks = new Category(1L, "Drinks", null);
        Category softs = new Category(2L, "Softs", drinks);
        Category soda = new Category(3L, "Soda", softs);
        ItemDto itemDto = ItemDto.builder().id(1L).name("Cola").description("Sugary black drink").price("0.500")
                .category(soda).pictureUrl("cola.jpg").weight(100).stockAmount(50).build();
        //when
        when(itemService.getOneItem(1L)).thenReturn(itemDto);

        //then
        mockMvc.perform(get("/items/{id}", itemDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(itemDto.getName()))
                .andExpect(jsonPath("$.price").value(itemDto.getPrice()))
                .andExpect(jsonPath("$.description").value(itemDto.getDescription()));
    }
}
