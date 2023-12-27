package dev.aziz.grocerystore.mappers;

import dev.aziz.grocerystore.dtos.CategoryDto;
import dev.aziz.grocerystore.entities.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category categoryDtoToCategory(CategoryDto categoryDto);

    CategoryDto categoryToCategoryDto(Category category);

    List<CategoryDto> categoriesToCategoryDtos(List<Category> categories);

    List<Category> categoryDtosToCategories(List<CategoryDto> categoryDtos);
}
