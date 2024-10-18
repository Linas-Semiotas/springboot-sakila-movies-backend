package lt.ca.javau10.sakila.repositories;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import lt.ca.javau10.sakila.models.City;
import lt.ca.javau10.sakila.models.Country;

@DataJpaTest  // Configures an in-memory database and loads repository beans
public class CityRepositoryTest {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CountryRepository countryRepository;  // Ensure you have a repository for countries

    @Test
    public void testFindByCity() {
        // Given: Create and save a new country
        Country country = new Country("USA");
        countryRepository.save(country);  // Save the country first

        // Create and save a new city associated with the country
        City city = new City();
        city.setCity("New York");
        city.setCountry(country);  // Associate the city with the country
        cityRepository.save(city);

        // When: Retrieve the city by name
        Optional<City> foundCity = cityRepository.findByCity("New York");

        // Then: Verify the city was found and the details are correct
        assertTrue(foundCity.isPresent(), "City should be found");
        assertEquals("New York", foundCity.get().getCity());
        assertEquals("USA", foundCity.get().getCountry().getCountry());
    }

    @Test
    public void testFindByCity_NotFound() {
        // When: Try to find a city that doesn't exist
        Optional<City> foundCity = cityRepository.findByCity("NonExistentCity");

        // Then: Verify the city is not found
        assertFalse(foundCity.isPresent(), "City should not be found");
    }

    @Test
    public void testExistsByCountry() {
        // Given: Create and save a new country
        Country country = new Country("USA");
        countryRepository.save(country);  // Save the country to the database

        // Create and save a city associated with the country
        City city = new City();
        city.setCity("Los Angeles");
        city.setCountry(country);  // Associate the city with the country
        cityRepository.save(city);

        // When: Check if a city exists by its associated country
        boolean exists = cityRepository.existsByCountry(country);

        // Then: Verify that a city exists for the given country
        assertTrue(exists, "City should exist for the given country");
    }

    @Test
    public void testExistsByCountry_NoCity() {
        // Given: Create a country without any associated city
        Country country = new Country("Japan");
        countryRepository.save(country);  // Save the country to the database

        // When: Check if any city exists for this country
        boolean exists = cityRepository.existsByCountry(country);

        // Then: Verify no city exists for the given country
        assertFalse(exists, "No city should exist for the given country");
    }
}