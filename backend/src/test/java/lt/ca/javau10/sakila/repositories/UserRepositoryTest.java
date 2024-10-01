package lt.ca.javau10.sakila.repositories;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import lt.ca.javau10.sakila.entities.User;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    
    private User user;

    @BeforeEach
    public void setUp() {
        // Initialize a new user before each test
        user = new User("john_doe", "password123", null);
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

        User foundUser = userRepository.findByUsername("john_doe");
        assertNotNull(foundUser);
        assertEquals("john_doe", foundUser.getUsername());
    }

    @Test
    public void testFindByUsername_NotFound() {
        User foundUser = userRepository.findByUsername("non_existing_user");
        assertNull(foundUser);
    }
}
