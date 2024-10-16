package lt.ca.javau10.sakila.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import lt.ca.javau10.sakila.models.Movie;
import lt.ca.javau10.sakila.models.dto.MovieDto;
import lt.ca.javau10.sakila.repositories.MovieRepository;

public class MovieServiceTest {

    @InjectMocks
    private MovieService movieService;

    @Mock
    private MovieRepository movieRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllMovies() {
        // Arrange
        Movie movie1 = new Movie();
        movie1.setFilmId((short) 1);
        movie1.setTitle("Inception");
        movie1.setDescription("A mind-bending thriller");
        movie1.setReleaseYear(2010);
        movie1.setFilmLength((short) 148);
        movie1.setRentalRate(BigDecimal.valueOf(3.99));
        movie1.setReplacementCost(BigDecimal.valueOf(19.99));
        movie1.setRentalDuration((short) 7);

        Movie movie2 = new Movie();
        movie2.setFilmId((short) 2);
        movie2.setTitle("The Matrix");
        movie2.setDescription("A computer hacker learns about the true nature of reality");
        movie2.setReleaseYear(1999);
        movie2.setFilmLength((short) 136);
        movie2.setRentalRate(BigDecimal.valueOf(2.99));
        movie2.setReplacementCost(BigDecimal.valueOf(14.99));
        movie2.setRentalDuration((short) 5);

        when(movieRepository.findAll()).thenReturn(Arrays.asList(movie1, movie2));

        // Act
        List<MovieDto> result = movieService.findAllMovies();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Inception", result.get(0).getTitle());
        assertEquals("The Matrix", result.get(1).getTitle());
    }

    @Test
    public void testGetMovieById_ExistingId() {
        // Arrange
        Movie movie = new Movie();
        movie.setFilmId((short) 1);
        movie.setTitle("Inception");
        movie.setDescription("A mind-bending thriller");
        movie.setReleaseYear(2010);
        movie.setFilmLength((short) 148);
        movie.setRentalRate(BigDecimal.valueOf(3.99));
        movie.setReplacementCost(BigDecimal.valueOf(19.99));
        movie.setRentalDuration((short) 7);

        when(movieRepository.findById((short) 1)).thenReturn(Optional.of(movie));

        // Act
        MovieDto result = movieService.findMovieById((short) 1);

        // Assert
        assertNotNull(result);
        assertEquals("Inception", result.getTitle());
    }

    @Test
    public void testGetMovieById_NonExistingId() {
        // Arrange
        when(movieRepository.findById((short) 999)).thenReturn(Optional.empty());

        // Act
        MovieDto result = movieService.findMovieById((short) 999);

        // Assert
        assertNull(result);
    }
}
