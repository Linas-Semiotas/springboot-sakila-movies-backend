package lt.ca.javau10.sakila;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import lt.ca.javau10.sakila.dto.MovieDto;
import lt.ca.javau10.sakila.entities.Actor;
import lt.ca.javau10.sakila.entities.Category;
import lt.ca.javau10.sakila.entities.Language;
import lt.ca.javau10.sakila.entities.Movie;
import lt.ca.javau10.sakila.repositories.MovieRepository;
import lt.ca.javau10.sakila.services.MovieService;

@SpringBootTest
class MovieServiceTests {
	
	@Mock
    private MovieRepository repository;

    @InjectMocks
    private MovieService service;
	
    
    @BeforeEach
    void setup() {
    	MockitoAnnotations.openMocks(this);
    }
    
    @Test
    void testGetMoviesById() {
    	//Arrange
        Short id = 1;
        Movie movie = new Movie();
        movie.setId(id);
        movie.setTitle("Mock Movie");
        
        Category mockCategory = new Category();
        mockCategory.setName("Action");
        movie.setCategory(mockCategory);
        
        Language mockLanguage = new Language();
        mockLanguage.setName("English");
        movie.setLanguage(mockLanguage);
        
        List<Actor> actors = new ArrayList<>();
        Actor actor = new Actor();
        actor.setFirstName("John");
        actor.setLastName("Doe");
        actors.add(actor);
        movie.setActors(actors);
        
        //Action
        when(repository.findById(id))
        	.thenReturn(Optional.of(movie));

        MovieDto movieDto = service.getMovieById(id);

        // Assertions
        assertNotNull(movieDto);
        assertEquals("Mock Movie", movieDto.getTitle()); 
        assertEquals("Action", movieDto.getCategory());
        assertEquals("English", movieDto.getLanguage());
    }
}
