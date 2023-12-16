package dev.aziz.grocerystore.controllers;

import dev.aziz.grocerystore.dtos.ItemDto;
import dev.aziz.grocerystore.dtos.ItemSummaryDto;
import dev.aziz.grocerystore.entities.Item;
import dev.aziz.grocerystore.repositories.ItemRepository;
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
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public ResponseEntity<List<ItemSummaryDto>> getAllItems() {
        return ResponseEntity.ok(itemService.getItemSummaries());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> getItem(@PathVariable Long id) {
        return ResponseEntity.ok(itemService.getOneItem(id));
    }
}
