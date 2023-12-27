package dev.aziz.grocerystore.services;

import dev.aziz.grocerystore.dtos.CategoryDto;
import dev.aziz.grocerystore.entities.Category;
import dev.aziz.grocerystore.exceptions.AppException;
import dev.aziz.grocerystore.mappers.CategoryMapper;
import dev.aziz.grocerystore.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public List<String> getAllCategoriesNames() {
        List<String> categories = categoryRepository.findAll().stream().map(Category::getName).toList();
        return categories;
    }

    public List<String> getAllSubcategoriesNames(String categoryName) {
        categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new AppException("Category not found.", HttpStatus.NOT_FOUND));
        List<String> subcategories = categoryRepository.getSubcategoriesOrGivenCategoryByName(categoryName)
                .orElseThrow(() -> new AppException("Category not found.", HttpStatus.NOT_FOUND));
        if (subcategories.isEmpty()) {
            throw new AppException("Subcategories not found.", HttpStatus.NOT_FOUND);
        }
        return subcategories;
    }

    public List<CategoryDto> getCategoriesByParentCategoryIsNull() {
        List<Category> categories = categoryRepository.getCategoriesByParentCategoryIsNull()
                .orElseThrow(() -> new AppException("Main categories not found.", HttpStatus.NOT_FOUND));
        return categoryMapper.categoriesToCategoryDtos(categories);
    }

    public List<CategoryDto> getCategoriesByParentCategoryIsNotNull() {
        List<Category> categories = categoryRepository.getCategoriesByParentCategoryIsNotNull()
                .orElseThrow(() -> new AppException("Main categories not found.", HttpStatus.NOT_FOUND));
        return categoryMapper.categoriesToCategoryDtos(categories);
    }
}
