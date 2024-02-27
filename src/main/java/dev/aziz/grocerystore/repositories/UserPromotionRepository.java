package dev.aziz.grocerystore.repositories;

import dev.aziz.grocerystore.entities.UserPromotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserPromotionRepository extends JpaRepository<UserPromotion, Long> {

    List<UserPromotion> findUserPromotionsByUserId(Long id);

    List<UserPromotion> findUserPromotionsByUserIdAndPromotionConfigEndDateAfter(Long id, Instant now);

}
