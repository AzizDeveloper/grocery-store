package dev.aziz.grocerystore.services;

import dev.aziz.grocerystore.dtos.BasketItemDto;
import dev.aziz.grocerystore.dtos.BasketTotalDto;
import dev.aziz.grocerystore.entities.BasketItem;
import dev.aziz.grocerystore.entities.Item;
import dev.aziz.grocerystore.entities.User;
import dev.aziz.grocerystore.entities.UserPromotion;
import dev.aziz.grocerystore.exceptions.AppException;
import dev.aziz.grocerystore.handlers.PriceHandler;
import dev.aziz.grocerystore.handlers.PriceHandlerContext;
import dev.aziz.grocerystore.mappers.BasketItemMapper;
import dev.aziz.grocerystore.repositories.BasketItemRepository;
import dev.aziz.grocerystore.repositories.ItemRepository;
import dev.aziz.grocerystore.repositories.UserPromotionRepository;
import dev.aziz.grocerystore.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class BasketItemService {

    private final BasketItemRepository basketItemRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final UserPromotionRepository userPromotionRepository;
    private final BasketItemMapper basketItemMapper;


    public BasketItemDto addItem(String login, Long itemId, Integer amount) {
        Item item = itemRepository.findItemById(itemId)
                .orElseThrow(() -> new AppException("Item not found.", HttpStatus.NOT_FOUND));

        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new AppException("User not found.", HttpStatus.NOT_FOUND));

        Optional<BasketItem> basketsByUserId = basketItemRepository
                .findBasketItemByUserIdAndItemId(user.getId(), item.getId());

        BasketItem saved;
        if (basketsByUserId.isPresent()) {
            saved = basketItemRepository.save(
                    BasketItem.builder()
                            .id(basketsByUserId.get().getId())
                            .user(user)
                            .item(item)
                            .amount(basketsByUserId.get().getAmount() + amount)
                            .build()
            );
        } else {
            saved = basketItemRepository.save(
                    BasketItem.builder()
                            .user(user)
                            .item(item)
                            .amount(amount)
                            .build()
            );
        }

        BasketItemDto basketItemDto = basketItemMapper.basketItemToBasketItemDto(saved);
        return basketItemDto;
    }

    public BasketTotalDto getItems(String login) {
        log.info("User {} is retrieving items from the basket.", login);

        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new AppException("User not found.", HttpStatus.NOT_FOUND));
        List<BasketItem> basketByUser = basketItemRepository.findBasketsByUserId(user.getId())
                .orElseThrow(() -> new AppException("Basket not found.", HttpStatus.NOT_FOUND));

        List<UserPromotion> userPromotionList = userPromotionRepository
                .findUserPromotionsByUserIdAndPromotionConfigEndDateAfter(user.getId(), Instant.now());

        List<BasketItemDto> basketItemDtoList = basketItemMapper.basketItemsToBasketItemDtos(basketByUser);

        PriceHandlerContext priceHandlerContext = new PriceHandlerContext(userPromotionList, basketByUser);

        PriceHandler priceHandler = new PriceHandler(priceHandlerContext);

        BasketTotalDto basketTotalDto = BasketTotalDto.builder()
                .basketItemDtos(basketItemDtoList)
                .wholeBasketPrice(priceHandler.computePrice().toString())
                .build();
        return basketTotalDto;
    }

    public List<BasketItemDto> deleteBasket(Long id) {
        List<BasketItem> basketByUserId = basketItemRepository.findBasketsByUserId(id)
                .orElseThrow(() -> new AppException("Basket not found.", HttpStatus.NOT_FOUND));
        basketItemRepository.deleteBasketItemsByUserId(id);
        log.info("The baskets of the user with id number {} have been deleted.", id);
        List<BasketItemDto> basketItemDtoList = basketItemMapper.basketItemsToBasketItemDtos(basketByUserId);
        return basketItemDtoList;
    }

}
