package dev.aziz.grocerystore.services;

import dev.aziz.grocerystore.dtos.ItemSummaryDto;
import dev.aziz.grocerystore.entities.Category;
import dev.aziz.grocerystore.entities.Item;
import dev.aziz.grocerystore.mappers.ItemMapper;
import dev.aziz.grocerystore.mappers.ItemMapperImpl;
import dev.aziz.grocerystore.repositories.CategoryRepository;
import dev.aziz.grocerystore.repositories.ItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Spy
    private ItemMapper itemMapper = new ItemMapperImpl();


    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ItemRepository itemRepository;

    @Test
    void getAllCategoriesNames() {
        //given
        Category drinks = new Category(1L, "Drinks", null);
        Category softs = new Category(2L, "Softs", drinks);
        Category soda = new Category(3L, "Soda", softs);
        Category tea = new Category(4L, "Tea", softs);
        Category alcohol = new Category(5L, "Alcohol", drinks);
        List<Category> categories = List.of(drinks, softs, soda, tea, alcohol);

        //when
        when(categoryRepository.findAll()).thenReturn(categories);
        List<String> categoriesNames = categoryService.getAllCategoriesNames();

        //then
        assertAll(() -> {
            assertEquals(categories.size(), categoriesNames.size());
            assertEquals(categories.get(0).getName(), categoriesNames.get(0));
        });
    }

    @Test
    void getAllSubcategoriesNames() {
        //given
        List<String> subCategories = List.of("Softs", "Soda", "Tea");
        String categoryName = "Soda";

        //when
        when(categoryRepository.getSubcategoriesOrGivenCategoryByName(categoryName)).thenReturn(Optional.of(subCategories));
        List<String> categoriesNames = categoryService.getAllSubcategoriesNames(categoryName);

        //then
        assertAll(() -> {
            assertEquals(subCategories.size(), categoriesNames.size());
            assertEquals(subCategories.get(0), categoriesNames.get(0));
        });
    }

    @Test
    void getItemsByCategories() {
        //given
        Category soda = new Category(3L, "Soda", null);
        Item cola = Item.builder().id(1L).name("Cola").description("Sugary black drink").price(BigDecimal.valueOf(0.500))
                .category(soda).pictureUrl("cola.jpg").weight(100).stockAmount(50).build();
        Item fanta = Item.builder().id(2L).name("Fanta").description("Sugary yellow drink").price(BigDecimal.valueOf(0.400))
                .category(soda).pictureUrl("fanta.jpg").weight(120).stockAmount(40).build();
        List<Item> items = List.of(cola, fanta);
        List<ItemSummaryDto> itemSummaryDtos = itemMapper.itemsToItemSummaryDtos(items);
        String categoryName = "Soda";

        //when
        when(itemRepository.findItemsByCategoryName(categoryName)).thenReturn(Optional.of(items));
        List<ItemSummaryDto> itemsByCategoryName = categoryService.getItemsByCategories(categoryName);

        //then
        assertAll(() -> {
            assertEquals(itemSummaryDtos.size(), itemsByCategoryName.size());
            assertEquals(itemSummaryDtos.get(0), itemsByCategoryName.get(0));
        });

    }
}