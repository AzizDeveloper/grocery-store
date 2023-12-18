package dev.aziz.grocerystore.mappers;

import dev.aziz.grocerystore.dtos.ItemDto;
import dev.aziz.grocerystore.dtos.ItemSummaryDto;
import dev.aziz.grocerystore.entities.Item;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemMapperTest {

    @Spy
    private ItemMapper itemMapper = new ItemMapperImpl();

    @Test
    void itemDtoToItemTest() {
        //given
        ItemDto itemDto = ItemDto.builder().id(1L).name("Apple").description("A fresh and juicy apple").price("0.500").category("Fruits").pictureUrl("apple.jpg").weight(100).stockAmount(50).build();
        //when
        Item item = itemMapper.itemDtoToItem(itemDto);
        //then
        assertAll(() -> {
            assertEquals(item.getName(), itemDto.getName());
            assertEquals(item.getWeight(), itemDto.getWeight());
            assertEquals(item.getPrice().toString(), itemDto.getPrice());
        });
    }

    @Test
    void itemToItemDtoTest() {
        //given
        Item appleItem = Item.builder().id(1L).name("Apple").description("A fresh and juicy apple").price(BigDecimal.valueOf(0.500)).category("Fruits").pictureUrl("apple.jpg").weight(100).stockAmount(50).build();

        //when
        ItemDto itemDto = itemMapper.itemToItemDto(appleItem);

        //then
        assertAll(() -> {
            assertEquals(appleItem.getName(), itemDto.getName());
            assertEquals(appleItem.getWeight(), itemDto.getWeight());
        });
    }

    @Test
    void itemsToItemDtosTest() {
        //given
        List<Item> items = new ArrayList<>();
        items.add(Item.builder().id(1L).name("Apple").description("A fresh and juicy apple").price(BigDecimal.valueOf(0.500)).category("Fruits").pictureUrl("apple.jpg").weight(100).stockAmount(50).build());
        items.add(Item.builder().id(2L).name("Banana").description("A ripe and sweet banana").price(BigDecimal.valueOf(0.400)).category("Fruits").pictureUrl("banana.jpg").weight(120).stockAmount(40).build());
        items.add(Item.builder().id(3L).name("Carrot").description("A crunchy and healthy carrot").price(BigDecimal.valueOf(0.300)).category("Vegetables").pictureUrl("carrot.jpg").weight(80).stockAmount(60).build());
        items.add(Item.builder().id(4L).name("Milk").description("A carton of fresh milk").price(BigDecimal.valueOf(1.000)).category("Dairy").pictureUrl("milk.jpg").weight(1000).stockAmount(20).build());
        items.add(Item.builder().id(5L).name("Cheese").description("A block of tasty cheese").price(BigDecimal.valueOf(2.000)).category("Dairy").pictureUrl("cheese.jpg").weight(500).stockAmount(30).build());
        //when
        List<ItemDto> itemDtos = itemMapper.itemsToItemDtos(items);
        //then
        assertAll(() -> {
            assertEquals(items.size(), itemDtos.size());
            assertEquals(items.get(0).getName(), itemDtos.get(0).getName());
            assertEquals(items.get(0).getWeight(), itemDtos.get(0).getWeight());
            assertEquals(items.get(3).getPrice().toString(), itemDtos.get(3).getPrice());
        });
    }

    @Test
    void itemToItemSummaryDtoTest() {
        //given
        Item appleItem = Item.builder().id(1L).name("Apple").description("A fresh and juicy apple").price(BigDecimal.valueOf(0.500)).category("Fruits").pictureUrl("apple.jpg").weight(100).stockAmount(50).build();

        //when
        ItemSummaryDto itemSummaryDto = itemMapper.itemToItemSummaryDto(appleItem);

        //then
        assertAll(() -> {
            assertEquals(appleItem.getName(), itemSummaryDto.getName());
            assertEquals(appleItem.getCategory(), itemSummaryDto.getCategory());
        });
    }

    @Test
    void itemsToItemSummaryDtos() {
        //given
        List<Item> items = new ArrayList<>();
        items.add(Item.builder().id(1L).name("Apple").description("A fresh and juicy apple").price(BigDecimal.valueOf(0.500)).category("Fruits").pictureUrl("apple.jpg").weight(100).stockAmount(50).build());
        items.add(Item.builder().id(2L).name("Banana").description("A ripe and sweet banana").price(BigDecimal.valueOf(0.400)).category("Fruits").pictureUrl("banana.jpg").weight(120).stockAmount(40).build());
        items.add(Item.builder().id(3L).name("Carrot").description("A crunchy and healthy carrot").price(BigDecimal.valueOf(0.300)).category("Vegetables").pictureUrl("carrot.jpg").weight(80).stockAmount(60).build());
        items.add(Item.builder().id(4L).name("Milk").description("A carton of fresh milk").price(BigDecimal.valueOf(1.000)).category("Dairy").pictureUrl("milk.jpg").weight(1000).stockAmount(20).build());
        items.add(Item.builder().id(5L).name("Cheese").description("A block of tasty cheese").price(BigDecimal.valueOf(2.000)).category("Dairy").pictureUrl("cheese.jpg").weight(500).stockAmount(30).build());
        //when
        List<ItemSummaryDto> itemSummaryDtos = itemMapper.itemsToItemSummaryDtos(items);
        //then
        assertAll(() -> {
            assertEquals(items.size(), itemSummaryDtos.size());
            assertEquals(items.get(0).getName(), itemSummaryDtos.get(0).getName());
            assertEquals(items.get(3).getPrice().toString(), itemSummaryDtos.get(3).getPrice());
        });
    }
}
