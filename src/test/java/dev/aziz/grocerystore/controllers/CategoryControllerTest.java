package dev.aziz.grocerystore.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.aziz.grocerystore.config.SecurityConfig;
import dev.aziz.grocerystore.config.UserAuthProvider;
import dev.aziz.grocerystore.config.UserAuthenticationEntryPoint;
import dev.aziz.grocerystore.dtos.CategoryDto;
import dev.aziz.grocerystore.dtos.ItemSummaryDto;
import dev.aziz.grocerystore.mappers.CategoryMapper;
import dev.aziz.grocerystore.mappers.CategoryMapperImpl;
import dev.aziz.grocerystore.mappers.ItemMapper;
import dev.aziz.grocerystore.mappers.ItemMapperImpl;
import dev.aziz.grocerystore.services.CategoryService;
import dev.aziz.grocerystore.services.ItemService;
import dev.aziz.grocerystore.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
class CategoryControllerTest {

    @Autowired
    public MockMvc mockMvc;

    @MockBean
    public CategoryService categoryService;

    @MockBean
    public ItemService itemService;

    @Autowired
    public ObjectMapper objectMapper;

    @Test
    void getAllCategoriesTest() throws Exception {
        //given
        List<String> categories = List.of("Drinks", "Softs", "Soda", "Tea", "Alcohol");

        //when
        when(categoryService.getAllCategoriesNames()).thenReturn(categories);

        //then
        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(categories.get(0)))
                .andExpect(jsonPath("$[1]").value(categories.get(1)));
    }

    @Test
    void getAllMainCategoriesTest() throws Exception {
        //given
        List<CategoryDto> categories = List.of(CategoryDto.builder().id(1L).name("Drinks").build());

        //when
        when(categoryService.getCategoriesByParentCategoryIsNull()).thenReturn(categories);

        //then
        mockMvc.perform(get("/categories/main"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(categories.get(0)))
                .andExpect(jsonPath("$[0].name").value(categories.get(0).getName()));
    }

    @Test
    void getSubcategoriesTest() throws Exception {
        //given
        List<String> subCategories = List.of("Soda", "Tea");
        String name = "Softs";

        //when
        when(categoryService.getAllSubcategoriesNames(name)).thenReturn(subCategories);

        //then
        mockMvc.perform(get("/categories/{name}/subcategories", name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(subCategories.get(0)))
                .andExpect(jsonPath("$[1]").value(subCategories.get(1)));
    }

    @Test
    void getItemsByCategoryNameTest() throws Exception {
        //given
        List<ItemSummaryDto> itemSummaryDtos = new ArrayList<>();
        CategoryDto soda = new CategoryDto(3L, "Soda");
        itemSummaryDtos.add(ItemSummaryDto.builder().id(1L).name("Cola").price("0.500").categoryDto(soda).build());
        itemSummaryDtos.add(ItemSummaryDto.builder().id(2L).name("Fanta").price("0.400").categoryDto(soda).build());
        String categoryName = "Soda";

        //when
        when(itemService.getItemsByCategoryName(categoryName)).thenReturn(itemSummaryDtos);

        //then
        mockMvc.perform(get("/categories/{name}/items", categoryName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(itemSummaryDtos.get(0)))
                .andExpect(jsonPath("$[1]").value(itemSummaryDtos.get(1)));
    }
}
