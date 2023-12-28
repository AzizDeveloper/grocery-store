package dev.aziz.grocerystore.controllers;

import dev.aziz.grocerystore.dtos.CategoryDto;
import dev.aziz.grocerystore.dtos.ItemSummaryDto;
import dev.aziz.grocerystore.services.CategoryService;
import dev.aziz.grocerystore.services.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final ItemService itemService;

    @GetMapping
    public ResponseEntity<List<String>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategoriesNames());
    }

    @GetMapping("/main")
    public ResponseEntity<List<CategoryDto>> getAllMainCategories() {
        return ResponseEntity.ok(categoryService.getCategoriesByParentCategoryIsNull());
    }

    @GetMapping("/subcategories")
    public ResponseEntity<List<CategoryDto>> getAllSubCategories() {
        return ResponseEntity.ok(categoryService.getCategoriesByParentCategoryIsNotNull());
    }

    @GetMapping("/{name}/subcategories")
    public ResponseEntity<List<String>> getSubcategories(@PathVariable String name) {
        return ResponseEntity.ok(categoryService.getAllSubcategoriesNames(name));
    }

    @GetMapping("/{name}/items")
    public ResponseEntity<List<ItemSummaryDto>> getItemsByCategoryName(@PathVariable String name) {
        return ResponseEntity.ok(itemService.getItemsByCategoryName(name));
    }
}
