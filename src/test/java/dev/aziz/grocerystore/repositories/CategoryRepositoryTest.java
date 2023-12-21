package dev.aziz.grocerystore.repositories;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
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
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void getSubcategoriesOrGivenCategoryByNameTest() {
        //given
        List<String> subCategories = List.of("Softs", "Soda", "Tea");
        String categoryName = "Softs";

        //when
        Optional<List<String>> subcategoriesNames = categoryRepository.getSubcategoriesOrGivenCategoryByName(categoryName);

        //then
        assertAll(() -> {
            assertEquals(subCategories.size(), subcategoriesNames.get().size());
            assertEquals(subCategories.get(0), subcategoriesNames.get().get(0));
        });
    }
}
