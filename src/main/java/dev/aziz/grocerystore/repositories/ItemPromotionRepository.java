package dev.aziz.grocerystore.repositories;

import dev.aziz.grocerystore.entities.ItemPromotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemPromotionRepository extends JpaRepository<ItemPromotion, Long> {

    List<ItemPromotion> findItemPromotionsByItemId(Long id);

}
