package dev.aziz.grocerystore.controllers;

import dev.aziz.grocerystore.dtos.ItemSummaryDto;
import dev.aziz.grocerystore.services.CategoryService;
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

    @GetMapping
    public ResponseEntity<List<String>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategoriesNames());
    }

    @GetMapping("/{name}/subcategories")
    public ResponseEntity<List<String>> getSubcategories(@PathVariable String name) {
        return ResponseEntity.ok(categoryService.getAllSubcategoriesNames(name));
    }

    @GetMapping("/{name}/items")
    public ResponseEntity<List<ItemSummaryDto>> getItemsByCategory(@PathVariable String name) {
        return ResponseEntity.ok(categoryService.getItemsByCategories(name));
    }

}
