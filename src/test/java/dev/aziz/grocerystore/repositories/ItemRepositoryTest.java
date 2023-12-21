package dev.aziz.grocerystore.repositories;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import dev.aziz.grocerystore.entities.Category;
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

    //TODO: I should change the test-schema.sql and test-data.sql scripts
    @Test
    void findMovieByIdTest() {
        //given
        Category drinks = new Category(1L, "Drinks", null);
        Category softs = new Category(2L, "Softs", drinks);
        Category soda = new Category(3L, "Soda", softs);
        Item item = Item.builder().id(1L).name("Cola").description("Sugary black drink").price(BigDecimal.valueOf(0.500))
                .category(soda).pictureUrl("cola.jpg").weight(100).stockAmount(50).build();

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
        //then
        List<Item> repositoryAll = itemRepository.findAll();
        assertAll(() -> {
            assertEquals(repositoryAll.size(), items.size());
            assertEquals(repositoryAll.get(0).getName(), items.get(0).getName());
        });
    }
}
