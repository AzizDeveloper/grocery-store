package dev.aziz.grocerystore.services;

import dev.aziz.grocerystore.dtos.ItemDto;
import dev.aziz.grocerystore.dtos.ItemSummaryDto;
import dev.aziz.grocerystore.entities.Item;
import dev.aziz.grocerystore.mappers.ItemMapper;
import dev.aziz.grocerystore.mappers.ItemMapperImpl;
import dev.aziz.grocerystore.repositories.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @InjectMocks
    private ItemService itemService;

    @Spy
    private ItemMapper itemMapper = new ItemMapperImpl();


    @Mock
    private ItemRepository itemRepository;

    private static List<Item> items = new ArrayList<>();


    @BeforeEach
    public void setUp() {
        items.clear();
        items.add(Item.builder().id(1L).name("Apple").description("A fresh and juicy apple").price(BigDecimal.valueOf(0.500)).category("Fruits").pictureUrl("apple.jpg").weight(100).stockAmount(50).build());
        items.add(Item.builder().id(2L).name("Banana").description("A ripe and sweet banana").price(BigDecimal.valueOf(0.400)).category("Fruits").pictureUrl("banana.jpg").weight(120).stockAmount(40).build());
        items.add(Item.builder().id(3L).name("Carrot").description("A crunchy and healthy carrot").price(BigDecimal.valueOf(0.300)).category("Vegetables").pictureUrl("carrot.jpg").weight(80).stockAmount(60).build());
        items.add(Item.builder().id(4L).name("Milk").description("A carton of fresh milk").price(BigDecimal.valueOf(1.000)).category("Dairy").pictureUrl("milk.jpg").weight(1000).stockAmount(20).build());
        items.add(Item.builder().id(5L).name("Cheese").description("A block of tasty cheese").price(BigDecimal.valueOf(2.000)).category("Dairy").pictureUrl("cheese.jpg").weight(500).stockAmount(30).build());
    }

    @Test
    void getItemSummariesTest() {
        //given
        //when
        when(itemRepository.findAll()).thenReturn(items);
        List<ItemSummaryDto> itemSummaryDtos = itemService.getItemSummaries();

        System.out.println(itemSummaryDtos);
        //then
        assertAll(() -> {
            assertEquals(5, itemSummaryDtos.size());
            assertEquals(itemMapper.itemsToItemSummaryDtos(items), itemSummaryDtos);
        });
    }

    @Test
    void getOneItemTest() {
        //given
        Item itemById2L = items.get(1);
        //when
        when(itemRepository.findItemById(2L)).thenReturn(Optional.ofNullable(itemById2L));
        ItemDto itemDto = itemService.getOneItem(2L);

        //then
        assertAll(() -> {
            assertEquals("Banana", itemDto.getName());
            assertEquals("Fruits", itemDto.getCategory());
        });
    }
}
