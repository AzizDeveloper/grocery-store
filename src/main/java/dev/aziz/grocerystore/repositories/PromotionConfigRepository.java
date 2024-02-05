package dev.aziz.grocerystore.repositories;

import dev.aziz.grocerystore.entities.PromotionConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionConfigRepository extends JpaRepository<PromotionConfig, Long> {

}
