package lt.ca.javau10.sakila.repositories;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;

import lt.ca.javau10.sakila.models.Inventory;
import lt.ca.javau10.sakila.models.Movie;
import lt.ca.javau10.sakila.models.Store;

@DataJpaTest  // Configure an in-memory database and load repository beans
public class InventoryRepositoryTest {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Test
    public void testFindFirstByMovieFilmIdAndStoreStoreId() {
        // Given: Create and save a new movie
        Movie movie = new Movie();
        movie.setFilmId((short) 1);  // Assuming filmId is of type Short
        movie.setTitle("Test Movie");
        movie.setRentalDuration((short) 5);  // Set rentalDuration (non-nullable)
        movie.setRentalRate(new BigDecimal("2.99"));  // Set rentalRate (non-nullable)
        movie.setReplacementCost(new BigDecimal("19.99"));  // Set replacementCost (non-nullable)
        movie = movieRepository.save(movie);  // Save the movie

        // Given: Create and save a new store
        Store store = new Store();
        store.setStoreId((byte) 1);  // Assuming storeId is of type Byte
        store = storeRepository.save(store);  // Save the store

        // Given: Create and save a new inventory associated with the movie and store
        Inventory inventory = new Inventory();
        inventory.setMovie(movie);  // Associate the movie with the inventory
        inventory.setStore(store);  // Associate the store with the inventory
        inventoryRepository.save(inventory);  // Save the inventory

        // When: Retrieve the inventory using the custom query method
        Optional<Inventory> foundInventory = inventoryRepository.findFirstByMovieFilmIdAndStoreStoreId(movie.getFilmId(), store.getStoreId());

        // Then: Verify the inventory was found and the details are correct
        assertTrue(foundInventory.isPresent(), "Inventory should be found");
        assertEquals(movie.getFilmId(), foundInventory.get().getMovie().getFilmId(), "Movie filmId should match");
        assertEquals(store.getStoreId(), foundInventory.get().getStore().getStoreId(), "Store storeId should match");
    }

    @Test
    public void testFindFirstByMovieFilmIdAndStoreStoreId_NotFound() {
        // When: Try to find inventory for a non-existent movie and store
        Optional<Inventory> foundInventory = inventoryRepository.findFirstByMovieFilmIdAndStoreStoreId((short) 99, (byte) 99);

        // Then: Verify the inventory is not found
        assertFalse(foundInventory.isPresent(), "Inventory should not be found for non-existent movie or store");
    }
}
