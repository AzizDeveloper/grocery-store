package dev.aziz.grocerystore.services;

import dev.aziz.grocerystore.dtos.ItemDto;
import dev.aziz.grocerystore.dtos.ItemSummaryDto;
import dev.aziz.grocerystore.entities.Item;
import dev.aziz.grocerystore.exceptions.AppException;
import dev.aziz.grocerystore.mappers.ItemMapper;
import dev.aziz.grocerystore.repositories.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    public List<ItemSummaryDto> getItemSummaries() {
        return itemMapper.itemsToItemSummaryDtos(itemRepository.findAll());
    }

    public ItemDto getOneItem(Long id) {
        return itemMapper.itemToItemDto(itemRepository.findItemById(id)
                .orElseThrow(() -> new AppException("Item not found.", HttpStatus.NOT_FOUND)));
    }

    public List<ItemSummaryDto> getItemsByCategoryName(String categoryName) {
        List<Item> items = itemRepository.findItemsByCategoryName(categoryName)
                .orElseThrow(() -> new AppException("Item not found.", HttpStatus.NOT_FOUND));
        List<ItemSummaryDto> itemSummaryDtos = itemMapper.itemsToItemSummaryDtos(items);
        return itemSummaryDtos;
    }

}
