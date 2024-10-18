package lt.ca.javau10.sakila.controllers;

import lt.ca.javau10.sakila.models.dto.MovieDto;
import lt.ca.javau10.sakila.security.utils.JwtUtil;
import lt.ca.javau10.sakila.services.CustomUserDetailsService;
import lt.ca.javau10.sakila.services.MovieService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MoviesController.class)
@AutoConfigureMockMvc(addFilters = false)
public class MoviesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;
    
    @MockBean
    private JwtUtil jwtUtil;
    
    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    private MovieDto movieDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Sample movie data for testing
        movieDto = new MovieDto((short) 1, "Movie1", "Description1", 2023, (short) 120, null, 
                "English", Arrays.asList("Action"), "Deleted Scenes", new BigDecimal("4.99"), 
                new BigDecimal("19.99"), (short) 3, Arrays.asList("John Doe"));
    }

    @Test
    public void testGetAllMovies() throws Exception {
        List<MovieDto> movies = Arrays.asList(movieDto);

        when(movieService.findAllMovies()).thenReturn(movies);

        mockMvc.perform(get("/api/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Movie1"))
                .andExpect(jsonPath("$[0].description").value("Description1"));
    }

    @Test
    public void testGetMovieById() throws Exception {
        when(movieService.findMovieById((short) 1)).thenReturn(movieDto);

        mockMvc.perform(get("/api/movies/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Movie1"))
                .andExpect(jsonPath("$.description").value("Description1"));
    }
}