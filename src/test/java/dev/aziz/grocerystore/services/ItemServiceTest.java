package dev.aziz.grocerystore.services;

import dev.aziz.grocerystore.dtos.ItemDto;
import dev.aziz.grocerystore.dtos.ItemSummaryDto;
import dev.aziz.grocerystore.entities.Category;
import dev.aziz.grocerystore.entities.Item;
import dev.aziz.grocerystore.mappers.CategoryMapper;
import dev.aziz.grocerystore.mappers.CategoryMapperImpl;
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
import org.springframework.test.util.ReflectionTestUtils;

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

    @Spy
    private CategoryMapper categoryMapper = new CategoryMapperImpl();

    @Mock
    private ItemRepository itemRepository;

    private static List<Item> items = new ArrayList<>();


    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(itemMapper, "categoryMapper", categoryMapper);
        items.clear();
        Category drinks = new Category(1L, "Drinks", null);
        Category softs = new Category(2L, "Softs", drinks);
        Category soda = new Category(3L, "Soda", softs);
        Category tea = new Category(4L, "Tea", softs);
        Category alcohol = new Category(5L, "Alcohol", drinks);
        items.add(Item.builder().id(1L).name("Cola").description("Sugary black drink").price(BigDecimal.valueOf(0.500))
                .category(soda).pictureUrl("cola.jpg").weight(100).stockAmount(50).build());
        items.add(Item.builder().id(2L).name("Fanta").description("Sugary yellow drink").price(BigDecimal.valueOf(0.400))
                .category(soda).pictureUrl("fanta.jpg").weight(120).stockAmount(40).build());
        items.add(Item.builder().id(3L).name("Black tea").description("Black natural tea").price(BigDecimal.valueOf(0.300))
                .category(tea).pictureUrl("blacktea.jpg").weight(80).stockAmount(60).build());
        items.add(Item.builder().id(4L).name("Wine").description("An alcoholic drink made from fermented fruit.").price(BigDecimal.valueOf(0.800))
                .category(alcohol).pictureUrl("wine.jpg").weight(30).stockAmount(20).build());
    }

    @Test
    void getItemSummariesTest() {
        //given
        //when
        when(itemRepository.findAll()).thenReturn(items);
        List<ItemSummaryDto> itemSummaryDtos = itemService.getItemSummaries();

        //then
        assertAll(() -> {
            assertEquals(items.size(), itemSummaryDtos.size());
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
            assertEquals(items.get(1).getName(), itemDto.getName());
            assertEquals(items.get(1).getCategory().getName(), itemDto.getCategoryDto().getName());
        });
    }
}
