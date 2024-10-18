package lt.ca.javau10.sakila.repositories;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.Set;

import lt.ca.javau10.sakila.models.User;

@DataJpaTest  // Configures an in-memory database and loads repository beans
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByUsername() {
        // Given: Create and save a new user
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setBalance(100.0);  // Ensure all fields are set
        user.setRoles(Set.of("ROLE_USER"));
        user = userRepository.save(user);  // Save user

        // When: Find the user by username
        Optional<User> foundUser = userRepository.findByUsername("testuser");

        // Then: Verify the user was found and the details are correct
        assertTrue(foundUser.isPresent(), "User should be found");
        assertEquals("testuser", foundUser.get().getUsername(), "Username should match");
        assertEquals("password123", foundUser.get().getPassword(), "Password should match");
    }

    @Test
    public void testFindByUsername_NotFound() {
        // When: Try to find a user with a non-existent username
        Optional<User> foundUser = userRepository.findByUsername("nonexistentuser");

        // Then: Verify the user is not found
        assertFalse(foundUser.isPresent(), "User should not be found for a non-existent username");
    }
}
