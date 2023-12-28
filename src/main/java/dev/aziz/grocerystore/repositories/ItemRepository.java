package dev.aziz.grocerystore.repositories;

import dev.aziz.grocerystore.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Optional<Item> findItemById(Long id);

    @Query(nativeQuery = true, value = "    WITH RECURSIVE category_tree AS (\n" +
            "            SELECT id, name, parent_category_id\n" +
            "            FROM category\n" +
            "                    WHERE name = :categoryName" +
            "                    UNION ALL\n" +
            "                    SELECT c.id, c.name, c.parent_category_id\n" +
            "                    FROM category c\n" +
            "                    INNER JOIN category_tree ct ON c.parent_category_id = ct.id\n" +
            "    )\n" +
            "    SELECT i.*\n" +
            "    FROM item i\n" +
            "    INNER JOIN category_tree ct ON i.category_id = ct.id;")
    Optional<List<Item>> findItemsByCategoryName(String categoryName);

}
