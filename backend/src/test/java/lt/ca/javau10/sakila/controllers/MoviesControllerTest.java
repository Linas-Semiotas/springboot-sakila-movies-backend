package lt.ca.javau10.sakila.controllers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import lt.ca.javau10.sakila.dto.MovieDto;
import lt.ca.javau10.sakila.entities.Rating;
import lt.ca.javau10.sakila.services.MovieService;

@WebMvcTest(MoviesController.class)
public class MoviesControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;

    @InjectMocks
    private MoviesController moviesController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(moviesController).build();

        MovieDto movie1 = new MovieDto((short) 1, "Movie One", "Description of Movie One", 2020, (short) 120, Rating.PG, "English", "Action", Arrays.asList("Actor One", "Actor Two"));
        MovieDto movie2 = new MovieDto((short) 2, "Movie Two", "Description of Movie Two", 2021, (short) 150, Rating.R, "English", "Drama", Arrays.asList("Actor Three", "Actor Four"));
        List<MovieDto> movies = Arrays.asList(movie1, movie2);

        when(movieService.getAllMovies()).thenReturn(movies);
        when(movieService.getMovieById((short) 1)).thenReturn(movies.get(0));
    }
    
    @Test
    public void getAllMovies_ShouldReturnListOfMovies() throws Exception {
        mockMvc.perform(get("/api/movies")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(movieService, times(1)).getAllMovies();
    }
    
    @Test
    public void getMovieById_ShouldReturnMovie() throws Exception {
        Short movieId = 1;

        mockMvc.perform(get("/api/movies/{id}", movieId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(movieService, times(1)).getMovieById(movieId);
    }
}