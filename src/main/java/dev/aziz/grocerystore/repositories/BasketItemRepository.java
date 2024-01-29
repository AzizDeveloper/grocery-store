package dev.aziz.grocerystore.repositories;

import dev.aziz.grocerystore.entities.BasketItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface BasketItemRepository extends JpaRepository<BasketItem, Long> {

    Optional<List<BasketItem>> findBasketsByUserId(Long id);

    Optional<BasketItem> findBasketItemByUserId(Long id);

    Optional<BasketItem> findBasketItemByUserIdAndItemId(Long userId, Long ItemId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,
            value = "DELETE FROM basket_item WHERE user_id = :id ;"
    )
    void deleteBasketItemsByUserId(@Param("id") Long id);
}
