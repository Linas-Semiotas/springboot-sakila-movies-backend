package lt.ca.javau10.sakila.controllers;

import lt.ca.javau10.sakila.models.Movie;
import lt.ca.javau10.sakila.models.dto.*;
import lt.ca.javau10.sakila.security.utils.JwtUtil;
import lt.ca.javau10.sakila.services.AdminService;
import lt.ca.javau10.sakila.services.CustomUserDetailsService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AdminControllerTest {

	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @MockBean
    private JwtUtil jwtUtil;
    
    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    public void setUp() {
    	MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllUsers() throws Exception {
        List<AdminUserDto> users = Arrays.asList(new AdminUserDto(1, "admin", true, true, true));
        when(adminService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(1))
                .andExpect(jsonPath("$[0].username").value("admin"))
                .andExpect(jsonPath("$[0].userRole").value(true))
                .andExpect(jsonPath("$[0].adminRole").value(true))
                .andExpect(jsonPath("$[0].enabled").value(true));
    }

    @Test
    public void testUpdateUser() throws Exception {
        AdminUserDto updateUserDto = new AdminUserDto(1, "updatedUser", true, true, false);
        doNothing().when(adminService).updateUser(1, updateUserDto);

        mockMvc.perform(put("/api/admin/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updateUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User updated successfully"));
    }

    @Test
    public void testGetAllMovies() throws Exception {
        List<MovieDto> movies = Arrays.asList(new MovieDto((short) 1, "Movie1", "Description1", 2023, (short) 120, null, 
                "English", Arrays.asList("Action"), "Deleted Scenes", new BigDecimal("4.99"), 
                new BigDecimal("19.99"), (short) 3, Arrays.asList("John Doe")));
        
        when(adminService.getAllMovies()).thenReturn(movies);

        mockMvc.perform(get("/api/admin/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Movie1"))
                .andExpect(jsonPath("$[0].description").value("Description1"))
                .andExpect(jsonPath("$[0].releaseYear").value(2023));
    }

    @Test
    public void testAddMovie() throws Exception {
        MovieDto newMovieDto = new MovieDto((short) 1, "New Movie", "New Description", 2023, (short) 120, null, 
                "English", Arrays.asList("Drama"), "Behind the Scenes", new java.math.BigDecimal("3.99"), 
                new java.math.BigDecimal("14.99"), (short) 5, Arrays.asList("Jane Smith"));
        
        Movie newMovie = new Movie();
        newMovie.setFilmId((short) 1);
        newMovie.setTitle("New Movie");
        newMovie.setDescription("New Description");
        
        when(adminService.addMovie(newMovieDto)).thenReturn(newMovie);

        mockMvc.perform(post("/api/admin/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(newMovieDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Movie added successfully"));
    }

    @Test
    public void testDeleteMovie() throws Exception {
        Short movieId = 1;
        doNothing().when(adminService).deleteMovie(movieId);

        mockMvc.perform(delete("/api/admin/movies/{id}", movieId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Movie deleted successfully"));
    }

    @Test
    public void testGetAllLanguages() throws Exception {
        List<LanguageDto> languages = Arrays.asList(new LanguageDto((byte) 1, "English"));
        when(adminService.getAllLanguages()).thenReturn(languages);

        mockMvc.perform(get("/api/admin/languages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("English"));
    }

    @Test
    public void testAddLanguage() throws Exception {
        LanguageDto newLanguage = new LanguageDto((byte) 1, "Spanish");
        when(adminService.addLanguage(newLanguage)).thenReturn(newLanguage);

        mockMvc.perform(post("/api/admin/languages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(newLanguage)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Language added successfully"));
    }

    @Test
    public void testDeleteLanguage() throws Exception {
        Short languageId = 1;
        doNothing().when(adminService).deleteLanguage(languageId);

        mockMvc.perform(delete("/api/admin/languages/{id}", languageId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Language deleted successfully"));
    }

    @Test
    public void testGetAllCategories() throws Exception {
        List<CategoryDto> categories = Arrays.asList(new CategoryDto((byte) 1, "Action"));
        when(adminService.getAllCategories()).thenReturn(categories);

        mockMvc.perform(get("/api/admin/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Action"));
    }

    @Test
    public void testAddCategory() throws Exception {
        CategoryDto newCategory = new CategoryDto((byte) 1, "Drama");
        when(adminService.addCategory(newCategory)).thenReturn(newCategory);

        mockMvc.perform(post("/api/admin/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(newCategory)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Category added successfully"));
    }

    @Test
    public void testDeleteCategory() throws Exception {
        Short categoryId = 1;
        doNothing().when(adminService).deleteCategory(categoryId);

        mockMvc.perform(delete("/api/admin/categories/{id}", categoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Category deleted successfully"));
    }

    @Test
    public void testGetAllActors() throws Exception {
        List<ActorDto> actors = Arrays.asList(new ActorDto((short) 1, "John", "Doe"));
        when(adminService.getAllActors()).thenReturn(actors);

        mockMvc.perform(get("/api/admin/actors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"));
    }

    @Test
    public void testAddActor() throws Exception {
        ActorDto newActor = new ActorDto((short) 1, "Jane", "Smith");
        when(adminService.addActor(newActor)).thenReturn(newActor);

        mockMvc.perform(post("/api/admin/actors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(newActor)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Actor added successfully"));
    }

    @Test
    public void testDeleteActor() throws Exception {
        Short actorId = 1;

        doNothing().when(adminService).deleteActor(actorId);

        mockMvc.perform(delete("/api/admin/actors/{id}", actorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Actor deleted successfully"));

        verify(adminService).deleteActor(actorId);
    }
}
