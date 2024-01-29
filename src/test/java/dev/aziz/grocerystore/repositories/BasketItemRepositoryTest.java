package dev.aziz.grocerystore.repositories;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import dev.aziz.grocerystore.entities.BasketItem;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})
class BasketItemRepositoryTest {

    @Autowired
    private BasketItemRepository basketItemRepository;

    @Test
    @Transactional
    void deleteBasketItemsByUserIdTest() {

        //given
        Long userId = 2L;
        Optional<List<BasketItem>> basketItemList = basketItemRepository.findBasketsByUserId(userId);
        List<BasketItem> allBeforeDeleting = basketItemRepository.findAll();

        //when
        basketItemRepository.deleteBasketItemsByUserId(userId);

        List<BasketItem> allAfterDeleting = basketItemRepository.findAll();

        //then
        List<BasketItem> basketItems = basketItemRepository.findBasketsByUserId(userId).orElse(new ArrayList<>());

        assertTrue(basketItems.isEmpty());
        assertNotEquals(allAfterDeleting, allBeforeDeleting);
    }
}
