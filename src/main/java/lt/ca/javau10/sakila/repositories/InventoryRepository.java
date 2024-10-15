package lt.ca.javau10.sakila.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lt.ca.javau10.sakila.models.Inventory;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
    Optional<Inventory> findFirstByMovieFilmIdAndStoreStoreId(Short filmId, Byte storeId);
}
