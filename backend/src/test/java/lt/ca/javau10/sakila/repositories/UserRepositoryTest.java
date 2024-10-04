package lt.ca.javau10.sakila.repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import lt.ca.javau10.sakila.models.User;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    
    private User user;

    @BeforeEach
    public void setUp() {
        // Initialize a new user before each test
        user = new User("john_doe", "password123", null, Set.of("USER"));
    }

    @Test
    public void testSaveUser() {
        User savedUser = userRepository.save(user);
        assertNotNull(savedUser);
        assertEquals("john_doe", savedUser.getUsername());
    }

    @Test
    public void testFindByUsername() {
        userRepository.save(user); // Save the user first

        Optional<User> foundUser = userRepository.findByUsername("john_doe");
        
        // Check that the user is found (Optional is present)
        assertTrue(foundUser.isPresent());
        
        // Extract the user from Optional and verify the username
        assertEquals("john_doe", foundUser.get().getUsername());
    }

    @Test
    public void testFindByUsername_NotFound() {
        Optional<User> foundUser = userRepository.findByUsername("non_existing_user");
        
        // Check that the Optional is empty (user not found)
        assertTrue(foundUser.isEmpty());
    }
}
