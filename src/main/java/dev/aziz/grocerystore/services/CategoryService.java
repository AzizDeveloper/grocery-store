package dev.aziz.grocerystore.services;

import dev.aziz.grocerystore.dtos.ItemSummaryDto;
import dev.aziz.grocerystore.entities.Category;
import dev.aziz.grocerystore.entities.Item;
import dev.aziz.grocerystore.exceptions.AppException;
import dev.aziz.grocerystore.mappers.ItemMapper;
import dev.aziz.grocerystore.repositories.CategoryRepository;
import dev.aziz.grocerystore.repositories.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    public List<String> getAllCategoriesNames() {
        List<String> categories = categoryRepository.findAll().stream().map(Category::getName).toList();
        return categories;
    }

    public List<String> getAllSubcategoriesNames(String categoryName) {
        List<String> subcategories = categoryRepository.getSubcategoriesOrGivenCategoryByName(categoryName)
                .orElseThrow(() -> new AppException("Category not found.", HttpStatus.NOT_FOUND));
        return subcategories;
    }

    public List<ItemSummaryDto> getItemsByCategories(String searchingCategory) {
        List<Item> itemsByCategoryName = itemRepository.findItemsByCategoryName(searchingCategory)
                .orElseThrow(() -> new AppException("Category not found.", HttpStatus.NOT_FOUND));
        List<ItemSummaryDto> itemSummaryDtos = itemMapper.itemsToItemSummaryDtos(itemsByCategoryName);
        return itemSummaryDtos;
    }
}
