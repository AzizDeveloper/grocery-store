package dev.aziz.grocerystore.repositories;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import dev.aziz.grocerystore.entities.Item;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void findMovieByIdTest() {
        //given
        Item item = Item.builder().id(1L).name("Apple").description("A fresh and juicy apple").price(BigDecimal.valueOf(0.500)).category("Fruits").pictureUrl("apple.jpg").weight(100).stockAmount(50).build();

        //when
        Optional<Item> foundItem = itemRepository.findItemById(1L);

        //then
        assertAll(() -> {
            assertTrue(foundItem.isPresent());
            assertEquals(item.getName(), foundItem.get().getName());
            assertEquals(item.getDescription(), foundItem.get().getDescription());
        });
    }

    @Test
    void findAllTest() {
        //given
        List<Item> items = new ArrayList<>();
        items.add(Item.builder().id(1L).name("Apple").description("A fresh and juicy apple").price(BigDecimal.valueOf(0.500)).category("Fruits").pictureUrl("apple.jpg").weight(100).stockAmount(50).build());
        items.add(Item.builder().id(2L).name("Banana").description("A ripe and sweet banana").price(BigDecimal.valueOf(0.400)).category("Fruits").pictureUrl("banana.jpg").weight(120).stockAmount(40).build());
        items.add(Item.builder().id(3L).name("Carrot").description("A crunchy and healthy carrot").price(BigDecimal.valueOf(0.300)).category("Vegetables").pictureUrl("carrot.jpg").weight(80).stockAmount(60).build());
        items.add(Item.builder().id(4L).name("Milk").description("A carton of fresh milk").price(BigDecimal.valueOf(1.000)).category("Dairy").pictureUrl("milk.jpg").weight(1000).stockAmount(20).build());
        items.add(Item.builder().id(5L).name("Cheese").description("A block of tasty cheese").price(BigDecimal.valueOf(2.000)).category("Dairy").pictureUrl("cheese.jpg").weight(500).stockAmount(30).build());
        //when
        //then
        List<Item> repositoryAll = itemRepository.findAll();
        assertAll(() -> {
            assertEquals(repositoryAll.size(), 5);
            assertEquals(repositoryAll.get(0).getName(), items.get(0).getName());
        });
    }
}
