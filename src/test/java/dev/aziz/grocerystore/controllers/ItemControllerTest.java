package dev.aziz.grocerystore.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.aziz.grocerystore.dtos.ItemDto;
import dev.aziz.grocerystore.dtos.ItemSummaryDto;
import dev.aziz.grocerystore.services.ItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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
        itemSummaryDtos.add(new ItemSummaryDto(1L, "Apple", "0.50", "Fruits"));
        itemSummaryDtos.add(new ItemSummaryDto(2L, "Banana", "0.40", "Fruits"));
        itemSummaryDtos.add(new ItemSummaryDto(3L, "Carrot", "0.30", "Vegetables"));
        itemSummaryDtos.add(new ItemSummaryDto(4L, "Milk", "1.00", "Dairy"));
        itemSummaryDtos.add(new ItemSummaryDto(5L, "Cheese", "2.00", "Dairy"));

        //when
        when(itemService.getItemSummaries()).thenReturn(itemSummaryDtos);

        //then
        mockMvc.perform(get("/items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Apple"))
                .andExpect(jsonPath("$[4].name").value("Cheese"))
                .andExpect(jsonPath("$[2].price").value(0.3))
                .andExpect(jsonPath("$[0].name").value("Apple"));
    }

    @Test
    void getOneItemTest() throws Exception {
        //given
        ItemDto bananaItemDto = ItemDto.builder()
                .id(2L)
                .name("Banana")
                .description("A ripe and sweet banana")
                .price("0.300")
                .category("Fruits")
                .pictureUrl("carrot.jpg")
                .weight(80)
                .stockAmount(60)
                .build();
        //when

        when(itemService.getOneItem(2L)).thenReturn(bananaItemDto);
        //then
        mockMvc.perform(get("/items/{id}", bananaItemDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Banana"))
                .andExpect(jsonPath("$.price").value("0.300"))
                .andExpect(jsonPath("$.description").value("A ripe and sweet banana"));
    }
}
