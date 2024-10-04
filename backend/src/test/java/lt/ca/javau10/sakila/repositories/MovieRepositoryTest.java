package lt.ca.javau10.sakila.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import lt.ca.javau10.sakila.models.Movie;

@DataJpaTest
public class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    private Movie movie;
    
    @BeforeEach
    public void setUp() {
        movie = new Movie();
        movie.setId((short) 1);
        movie.setTitle("Test Movie");
        movie.setDescription("A description of the test movie.");
        movie.setReleaseYear(2024);
        movie.setFilmLength((short) 120);
        movie.setRentalRate(new BigDecimal("2.99"));
        movie.setReplacementCost(new BigDecimal("19.99"));
        movie.setRentalDuration((short) 5);
        // Set any necessary relationships with other entities (language, category, actors) if applicable
    }

    @Test
    public void testSaveMovie() {
        Movie savedMovie = movieRepository.save(movie);
        assertNotNull(savedMovie);
        assertThat(savedMovie.getId()).isEqualTo(movie.getId());
    }

    @Test
    public void testFindById() {
        movieRepository.save(movie);
        Movie foundMovie = movieRepository.findById(movie.getId()).orElse(null);
        assertNotNull(foundMovie);
        assertThat(foundMovie.getTitle()).isEqualTo(movie.getTitle());
    }

    @Test
    public void testFindAll() {
        movieRepository.save(movie);
        List<Movie> movies = movieRepository.findAll();
        assertThat(movies).isNotEmpty();
        assertThat(movies.size()).isEqualTo(1);
    }

    @Test
    public void testDeleteMovie() {
        movieRepository.save(movie);
        movieRepository.delete(movie);
        List<Movie> movies = movieRepository.findAll();
        assertThat(movies).isEmpty();
    }
}
