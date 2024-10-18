package lt.ca.javau10.sakila.repositories;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import lt.ca.javau10.sakila.models.Customer;
import lt.ca.javau10.sakila.models.Inventory;
import lt.ca.javau10.sakila.models.Rental;
import lt.ca.javau10.sakila.models.Store;
import lt.ca.javau10.sakila.models.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@DataJpaTest  // Configures an in-memory database and loads repository beans
public class RentalRepositoryTest {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Test
    public void testFindAllByUserId() {
        // Given: Create and save a new user
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setBalance(100.0);
        user.setRoles(Set.of("ROLE_USER"));
        user = userRepository.save(user);  // Save the user and get userId

        // Given: Create and save a new customer associated with the user
        Customer customer = new Customer();
        customer.setUser(user);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("john.doe@example.com");
        customer.setStoreId((byte) 1);
        customer.setActive((byte) 1);
        customer = customerRepository.save(customer);

        // Given: Create and save a new store
        Store store = new Store();
        store.setStoreId((byte) 1);  // Ensure the store has a valid storeId
        store = storeRepository.save(store);  // Save the store

        // Given: Create and save a new inventory associated with the store
        Inventory inventory = new Inventory();
        inventory.setStore(store);  // Associate the inventory with the store
        inventory = inventoryRepository.save(inventory);  // Save inventory

        // Given: Create and save two rentals associated with the user and customer
        Rental rental1 = new Rental();
        rental1.setCustomer(customer);
        rental1.setInventory(inventory);
        rental1.setRentalDate(LocalDateTime.now());
        rental1.setStaffId((byte) 1);
        rentalRepository.save(rental1);  // Save rental 1

        Rental rental2 = new Rental();
        rental2.setCustomer(customer);
        rental2.setInventory(inventory);
        rental2.setRentalDate(LocalDateTime.now().minusDays(1));
        rental2.setStaffId((byte) 2);
        rentalRepository.save(rental2);  // Save rental 2

        // When: Call the custom query method to find all rentals by userId
        List<Rental> rentals = rentalRepository.findAllByUserId(user.getUserId());

        // Then: Verify the results
        assertEquals(2, rentals.size(), "There should be 2 rentals for the user");
        assertEquals(rental1.getId(), rentals.get(0).getId(), "First rental ID should match");
        assertEquals(rental2.getId(), rentals.get(1).getId(), "Second rental ID should match");
    }

    @Test
    public void testFindAllByUserId_NotFound() {
        // When: Try to find rentals for a non-existent user
        List<Rental> rentals = rentalRepository.findAllByUserId(99);

        // Then: Verify no rentals are found
        assertTrue(rentals.isEmpty(), "No rentals should be found for a non-existent userId");
    }
}
