package dev.aziz.grocerystore.controllers;

import dev.aziz.grocerystore.dtos.BasketItemDto;
import dev.aziz.grocerystore.dtos.BasketTotalDto;
import dev.aziz.grocerystore.dtos.UserDto;
import dev.aziz.grocerystore.entities.BasketItem;
import dev.aziz.grocerystore.services.BasketItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/basket")
public class BasketItemController {

    private final BasketItemService basketItemService;

    @PostMapping("/items/{item_id}/amount/{amount}")
    public ResponseEntity<BasketItemDto> addItemToBasket(@AuthenticationPrincipal UserDto userDto,
                                                         @PathVariable Long item_id,
                                                         @PathVariable Integer amount) {
        return ResponseEntity.ok(basketItemService.addItem(userDto.getLogin(), item_id, amount));
    }

    @GetMapping
    public ResponseEntity<BasketTotalDto> getBasket(@AuthenticationPrincipal UserDto userDto) {
        return ResponseEntity.ok(basketItemService.getItems(userDto.getLogin()));
    }

    @DeleteMapping
    public ResponseEntity<List<BasketItemDto>> deleteBasket(@AuthenticationPrincipal UserDto userDto) {
        return ResponseEntity.ok(basketItemService.deleteBasket(userDto.getId()));
    }

}
