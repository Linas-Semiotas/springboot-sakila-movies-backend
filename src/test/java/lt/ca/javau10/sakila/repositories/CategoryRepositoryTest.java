package lt.ca.javau10.sakila.repositories;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Optional;
import lt.ca.javau10.sakila.models.Category;

@DataJpaTest  // Configures an in-memory database and loads repository beans
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testExistsByName() {
        // Given: Create and save a new category
        Category category = new Category();
        category.setName("Comedy");
        categoryRepository.save(category);

        // When: Check if the category exists by name
        boolean exists = categoryRepository.existsByName("Comedy");

        // Then: Verify the category exists
        assertTrue(exists, "Category should exist in the database");
    }

    @Test
    public void testDoesNotExistByName() {
        // When: Check for a category that doesn't exist
        boolean exists = categoryRepository.existsByName("NonExistent");

        // Then: Verify the category doesn't exist
        assertFalse(exists, "Category should not exist in the database");
    }

    @Test
    public void testFindByName() {
        // Given: Create and save a new category
        Category category = new Category();
        category.setName("Action");
        categoryRepository.save(category);

        // When: Retrieve the category by name
        Optional<Category> foundCategory = categoryRepository.findByName("Action");

        // Then: Verify the category was found and the details are correct
        assertTrue(foundCategory.isPresent(), "Category should be found");
        assertEquals("Action", foundCategory.get().getName());
    }

    @Test
    public void testFindByName_NotFound() {
        // When: Try to find a category that doesn't exist
        Optional<Category> foundCategory = categoryRepository.findByName("NonExistent");

        // Then: Verify the category is not found
        assertFalse(foundCategory.isPresent(), "Category should not be found");
    }
}

