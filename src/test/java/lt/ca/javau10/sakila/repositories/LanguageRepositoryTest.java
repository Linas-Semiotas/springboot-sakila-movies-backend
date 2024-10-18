package lt.ca.javau10.sakila.repositories;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import lt.ca.javau10.sakila.models.Language;

@DataJpaTest  // Configures an in-memory database and loads repository beans
public class LanguageRepositoryTest {

    @Autowired
    private LanguageRepository languageRepository;

    @Test
    public void testExistsByName() {
        // Given: Create and save a new language
        Language language = new Language();
        language.setName("English");
        language = languageRepository.save(language);  // Save the language

        // When: Check if the language exists by name
        boolean exists = languageRepository.existsByName("English");

        // Then: Verify the language exists
        assertTrue(exists, "Language should exist");
    }

    @Test
    public void testFindByName() {
        // Given: Create and save a new language
        Language language = new Language();
        language.setName("Spanish");
        language = languageRepository.save(language);  // Save the language

        // When: Find the language by name
        Optional<Language> foundLanguage = languageRepository.findByName("Spanish");

        // Then: Verify the language was found and the details are correct
        assertTrue(foundLanguage.isPresent(), "Language should be found");
        assertEquals("Spanish", foundLanguage.get().getName(), "Language name should match");
    }

    @Test
    public void testFindByName_NotFound() {
        // When: Try to find a language that doesn't exist
        Optional<Language> foundLanguage = languageRepository.findByName("French");

        // Then: Verify the language is not found
        assertFalse(foundLanguage.isPresent(), "Language should not be found");
    }
}
