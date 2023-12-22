package dev.aziz.grocerystore.mappers;

import dev.aziz.grocerystore.dtos.CategoryDto;
import dev.aziz.grocerystore.entities.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category categoryDtoToCategory(CategoryDto categoryDto);

    CategoryDto categoryToCategoryDto(Category category);
}
