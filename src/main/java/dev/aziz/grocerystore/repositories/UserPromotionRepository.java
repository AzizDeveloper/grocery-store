package dev.aziz.grocerystore.repositories;

import dev.aziz.grocerystore.entities.UserPromotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPromotionRepository extends JpaRepository<UserPromotion, Long> {

    Optional<UserPromotion> findByUserId(Long id);
}
