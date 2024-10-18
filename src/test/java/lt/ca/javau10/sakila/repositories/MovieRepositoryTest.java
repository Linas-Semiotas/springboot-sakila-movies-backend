package lt.ca.javau10.sakila.repositories;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import lt.ca.javau10.sakila.models.Actor;
import lt.ca.javau10.sakila.models.Category;
import lt.ca.javau10.sakila.models.Language;
import lt.ca.javau10.sakila.models.Movie;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;



@DataJpaTest  // Configures an in-memory database and loads repository beans
public class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private LanguageRepository languageRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private ActorRepository actorRepository;

    @Test
    public void testFindById() {
        // Given: Create and save a new movie
        Movie movie = new Movie();
        movie.setTitle("Test Movie");
        movie.setRentalDuration((short) 5);
        movie.setRentalRate(new BigDecimal("2.99"));
        movie.setReplacementCost(new BigDecimal("19.99"));
        movie = movieRepository.save(movie);  // Save the movie

        // When: Retrieve the movie by ID
        Optional<Movie> foundMovie = movieRepository.findById(movie.getFilmId());

        // Then: Verify the movie was found and the details are correct
        assertTrue(foundMovie.isPresent(), "Movie should be found");
        assertEquals("Test Movie", foundMovie.get().getTitle(), "Movie title should match");
    }

    @Test
    public void testExistsByLanguage() {
        // Given: Create and save a new language
        Language language = new Language();
        language.setName("English");
        language = languageRepository.save(language);

        // Given: Create and save a new movie with the language
        Movie movie = new Movie();
        movie.setTitle("English Movie");
        movie.setRentalDuration((short) 5);
        movie.setRentalRate(new BigDecimal("2.99"));
        movie.setReplacementCost(new BigDecimal("19.99"));
        movie.setLanguage(language);
        movie = movieRepository.save(movie);

        // When: Check if any movie exists with the given language
        boolean exists = movieRepository.existsByLanguage(language);

        // Then: Verify the movie exists
        assertTrue(exists, "Movie with the specified language should exist");
    }

    @Test
    public void testExistsByCategory() {
        // Given: Create and save a new category
        Category category = new Category();
        category.setName("Action");
        category = categoryRepository.save(category);

        // Given: Create and save a new movie with the category
        Movie movie = new Movie();
        movie.setTitle("Action Movie");
        movie.setRentalDuration((short) 5);
        movie.setRentalRate(new BigDecimal("2.99"));
        movie.setReplacementCost(new BigDecimal("19.99"));
        movie.setCategory(Collections.singletonList(category));  // Associate category with movie
        movie = movieRepository.save(movie);

        // When: Check if any movie exists with the given category
        boolean exists = movieRepository.existsByCategory(category);

        // Then: Verify the movie exists
        assertTrue(exists, "Movie with the specified category should exist");
    }

    @Test
    public void testExistsByActorsContaining() {
        // Given: Create and save a new actor
        Actor actor = new Actor();
        actor.setFirstName("John");
        actor.setLastName("Doe");
        actor = actorRepository.save(actor);

        // Given: Create and save a new movie with the actor
        Movie movie = new Movie();
        movie.setTitle("Movie with Actor");
        movie.setRentalDuration((short) 5);
        movie.setRentalRate(new BigDecimal("2.99"));
        movie.setReplacementCost(new BigDecimal("19.99"));
        movie.setActors(Collections.singletonList(actor));  // Associate actor with movie
        movie = movieRepository.save(movie);

        // When: Check if any movie contains the given actor
        boolean exists = movieRepository.existsByActorsContaining(actor);

        // Then: Verify the movie exists
        assertTrue(exists, "Movie with the specified actor should exist");
    }
}

