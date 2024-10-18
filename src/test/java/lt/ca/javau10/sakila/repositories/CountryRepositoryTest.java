package lt.ca.javau10.sakila.repositories;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import lt.ca.javau10.sakila.models.Country;

@DataJpaTest  // Configures an in-memory database and loads repository beans
public class CountryRepositoryTest {

    @Autowired
    private CountryRepository countryRepository;

    @Test
    public void testFindByCountry() {
        // Given: Create and save a new country
        Country country = new Country();
        country.setCountry("USA");
        countryRepository.save(country);  // Save the country entity

        // When: Retrieve the country by name
        Optional<Country> foundCountry = countryRepository.findByCountry("USA");

        // Then: Verify the country was found and the details are correct
        assertTrue(foundCountry.isPresent(), "Country should be found");
        assertEquals("USA", foundCountry.get().getCountry(), "Country name should be 'USA'");
    }

    @Test
    public void testFindByCountry_NotFound() {
        // When: Try to find a country that doesn't exist
        Optional<Country> foundCountry = countryRepository.findByCountry("NonExistentCountry");

        // Then: Verify the country is not found
        assertFalse(foundCountry.isPresent(), "Country should not be found");
    }
}