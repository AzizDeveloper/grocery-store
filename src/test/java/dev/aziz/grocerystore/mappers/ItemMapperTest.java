package dev.aziz.grocerystore.mappers;

import dev.aziz.grocerystore.dtos.CategoryDto;
import dev.aziz.grocerystore.dtos.ItemDto;
import dev.aziz.grocerystore.dtos.ItemSummaryDto;
import dev.aziz.grocerystore.entities.Category;
import dev.aziz.grocerystore.entities.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemMapperTest {

    @Spy
    private ItemMapper itemMapper = new ItemMapperImpl();

    @Spy
    private CategoryMapper categoryMapper = new CategoryMapperImpl();

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(itemMapper, "categoryMapper", categoryMapper);
    }

    @Test
    void itemDtoToItemTest() {
        //given
        CategoryDto soda = new CategoryDto(3L, "Soda");
        ItemDto itemDto = ItemDto.builder().id(1L).name("Cola").description("Sugary black drink")
                .price("0.500")
                .categoryDto(soda).pictureUrl("cola.jpg").weight(100).stockAmount(50).build();
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
        Category drinks = new Category(1L, "Drinks", null);
        Category softs = new Category(2L, "Softs", drinks);
        Category soda = new Category(3L, "Soda", softs);
        Item item = Item.builder().id(1L).name("Cola").description("Sugary black drink").price(BigDecimal.valueOf(0.500))
                .category(soda).pictureUrl("cola.jpg").weight(100).stockAmount(50).build();

        //when
        ItemDto itemDto = itemMapper.itemToItemDto(item);

        //then
        assertAll(() -> {
            assertEquals(item.getName(), itemDto.getName());
            assertEquals(item.getWeight(), itemDto.getWeight());
        });
    }

    @Test
    void itemsToItemDtosTest() {
        //given
        List<Item> items = new ArrayList<>();
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
        Category drinks = new Category(1L, "Drinks", null);
        Category softs = new Category(2L, "Softs", drinks);
        Category soda = new Category(3L, "Soda", softs);
        Item item = Item.builder().id(1L).name("Cola").description("Sugary black drink").price(BigDecimal.valueOf(0.500))
                .category(soda).pictureUrl("cola.jpg").weight(100).stockAmount(50).build();

        //when
        ItemSummaryDto itemSummaryDto = itemMapper.itemToItemSummaryDto(item);

        //then
        assertAll(() -> {
            assertEquals(item.getName(), itemSummaryDto.getName());
            assertEquals(item.getCategory().getName(), itemSummaryDto.getCategoryDto().getName());
        });
    }

    @Test
    void itemsToItemSummaryDtos() {
        //given
        List<Item> items = new ArrayList<>();
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
