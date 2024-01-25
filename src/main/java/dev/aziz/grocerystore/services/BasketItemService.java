package dev.aziz.grocerystore.services;

import dev.aziz.grocerystore.dtos.BasketItemDto;
import dev.aziz.grocerystore.dtos.BasketTotalDto;
import dev.aziz.grocerystore.entities.BasketItem;
import dev.aziz.grocerystore.entities.Item;
import dev.aziz.grocerystore.entities.User;
import dev.aziz.grocerystore.exceptions.AppException;
import dev.aziz.grocerystore.mappers.BasketItemMapper;
import dev.aziz.grocerystore.repositories.BasketItemRepository;
import dev.aziz.grocerystore.repositories.ItemRepository;
import dev.aziz.grocerystore.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class BasketItemService {

    private final BasketItemRepository basketItemRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BasketItemMapper basketItemMapper;


    public BasketItem addItem(String login, Long itemId, Integer amount) {
        Item item = itemRepository.findItemById(itemId)
                .orElseThrow(() -> new AppException("Item not found.", HttpStatus.NOT_FOUND));

        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new AppException("User not found.", HttpStatus.NOT_FOUND));
        BasketItem saved = basketItemRepository.save(
                BasketItem.builder()
                        .user(user)
                        .item(item)
                        .amount(amount)
                        .build()
        );
        return saved;
    }

    public BasketTotalDto getItems(String login) {
        log.info("User {} is retrieving items from the basket.", login);

        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new AppException("User not found.", HttpStatus.NOT_FOUND));
        List<BasketItem> basketByUser = basketItemRepository.findBasketsByUserId(user.getId())
                .orElseThrow(() -> new AppException("Basket not found.", HttpStatus.NOT_FOUND));

        List<BasketItemDto> basketItemDtoList = new ArrayList<>();
        for (BasketItem basketItem : basketByUser) {
            basketItemDtoList.add(basketItemMapper.basketItemToBasketItemDto(basketItem));
        }

        Map<String, List<BasketItemDto>> groupedItems = basketItemDtoList.stream()
                .collect(Collectors.groupingBy(BasketItemDto::getName));
        List<BasketItemDto> reducedItems = new ArrayList<>();
        for (Map.Entry<String, List<BasketItemDto>> entry : groupedItems.entrySet()) {
            BasketItemDto initial = new BasketItemDto();
            initial.setStockAmount(0);
            initial.setTotalPrice("0");
            BasketItemDto reducedItem = entry.getValue().stream().reduce(initial, (a, b) -> {
                a.setName(b.getName());
                a.setStockAmount(a.getStockAmount() + b.getStockAmount());
                a.setUnitPrice(b.getUnitPrice());
                a.setTotalPrice(new BigDecimal(a.getTotalPrice()).add(new BigDecimal(b.getTotalPrice())).toString());
                return a;
            });
            reducedItems.add(reducedItem);
        }

        BigDecimal wholeBasketPrice = basketItemDtoList.stream()
                .map(item -> new BigDecimal(item.getTotalPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BasketTotalDto basketTotalDto = BasketTotalDto.builder()
                .basketItemDtos(reducedItems)
                .wholeBasketPrice(wholeBasketPrice.toString())
                .build();
        return basketTotalDto;
    }

    public List<BasketItem> deleteBasket(Long id) {
        List<BasketItem> basketByUserId = basketItemRepository.findBasketsByUserId(id)
                .orElseThrow(() -> new AppException("Basket not found.", HttpStatus.NOT_FOUND));
        basketItemRepository.deleteBasketItemsByUserId(id);
        log.info("The baskets of the user with id number {} have been deleted.", id);
        return basketByUserId;
    }
}
