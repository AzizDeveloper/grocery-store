package dev.aziz.grocerystore.mappers;

import dev.aziz.grocerystore.dtos.CategoryDto;
import dev.aziz.grocerystore.entities.Category;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CategoryMapperTest {

    @Spy
    private CategoryMapper categoryMapper = new CategoryMapperImpl();

    @Test
    void categoryToCategoryDtoTest() {
        //given
        Category category = Category.builder()
                .id(1L)
                .name("Drinks")
                .parentCategory(null)
                .build();
        //when
        CategoryDto categoryDto = categoryMapper.categoryToCategoryDto(category);

        //then
        assertAll(() -> {
            assertEquals(category.getName(), categoryDto.getName());
            assertEquals(category.getId(), categoryDto.getId());
        });
    }

    @Test
    void categoriesToCategoryDtosTest() {
        //given
        List<Category> categories = new ArrayList<>();
        Category drinks = new Category(1L, "Drinks", null);
        Category softs = new Category(2L, "Softs", drinks);
        Category soda = new Category(3L, "Soda", softs);
        Category tea = new Category(4L, "Tea", softs);
        categories.add(drinks);
        categories.add(softs);
        categories.add(soda);
        categories.add(tea);

        //when
        List<CategoryDto> categoryDtos = categoryMapper.categoriesToCategoryDtos(categories);

        //then
        assertAll(() -> {
            assertEquals(categories.size(), categoryDtos.size());
            assertEquals(categories.get(0).getName(), categoryDtos.get(0).getName());
        });
    }
}
