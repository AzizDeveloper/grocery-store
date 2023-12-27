package dev.aziz.grocerystore.repositories;

import dev.aziz.grocerystore.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);

    @Query(nativeQuery = true, value =
            "WITH RECURSIVE category_tree(id, name, parent_category_id) AS (" +
                    "    SELECT id, name, parent_category_id FROM category" +
                    "    WHERE name = :category_name" +
                    "    UNION ALL" +
                    "    SELECT category.id, category.name, category.parent_category_id FROM category" +
                    "             INNER JOIN category_tree ON category.parent_category_id = category_tree.id)" +
                    "SELECT name FROM category_tree WHERE name != :category_name ;")
    Optional<List<String>> getSubcategoriesOrGivenCategoryByName(@Param("category_name") String category_name);

    Optional<List<Category>> getCategoriesByParentCategoryIsNull();

    Optional<List<Category>> getCategoriesByParentCategoryIsNotNull();

}
