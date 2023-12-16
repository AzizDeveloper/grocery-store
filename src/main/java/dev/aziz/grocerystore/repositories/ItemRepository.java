package dev.aziz.grocerystore.repositories;

import dev.aziz.grocerystore.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Optional<Item> findItemById(Long id);

}
