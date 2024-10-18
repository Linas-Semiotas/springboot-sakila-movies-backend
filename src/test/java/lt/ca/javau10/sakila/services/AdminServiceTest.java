package lt.ca.javau10.sakila.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import lt.ca.javau10.sakila.models.*;
import lt.ca.javau10.sakila.models.dto.*;
import lt.ca.javau10.sakila.repositories.*;

public class AdminServiceTest {

    @InjectMocks
    private AdminService adminService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private LanguageRepository languageRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ActorRepository actorRepository;

    @Mock
    private MovieRepository movieRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test for getAllUsers()
    @Test
    public void testGetAllUsers() {
    	 User user = new User();
	    setUserId(user, 1); // Use reflection to set the userId
	    user.setUsername("user1");
	    user.setRoles(Set.of("USER"));

	    Customer customer = new Customer();
	    customer.setActive((byte) 1); // Mock Customer
	    user.setCustomer(customer);

	    when(userRepository.findAll()).thenReturn(List.of(user));

	    List<AdminUserDto> users = adminService.getAllUsers();

	    assertNotNull(users);
	    assertEquals(1, users.size());
	    assertEquals("user1", users.get(0).getUsername());
	    assertTrue(users.get(0).isEnabled()); // Ensure customer active status is reflected
	    assertEquals(1, users.get(0).getUserId());  // Check the userId
    }
    
    private void setUserId(User user, int id) {
    	try {
            Field userIdField = User.class.getDeclaredField("userId");
            userIdField.setAccessible(true);
            userIdField.set(user, id);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e); // Convert checked exceptions to runtime exceptions
        }
    }


    // Test for updateUser()
    @Test
    public void testUpdateUser() {
        User user = new User();
        user.setRoles(new HashSet<>(Set.of("USER")));
        Customer customer = new Customer();
        customer.setActive((byte) 1);
        user.setCustomer(customer);

        AdminUserDto updateDto = new AdminUserDto();
        updateDto.setUserId(1);
        updateDto.setUsername("user1");
        updateDto.setUserRole(true);
        updateDto.setAdminRole(true);
        updateDto.setEnabled(true);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        adminService.updateUser(1, updateDto);

        verify(customerRepository).save(customer);
        verify(userRepository).save(user);
    }

    // Test for getAllMovies()
    @Test
    public void testGetAllMovies() {
        Movie movie = new Movie();
        movie.setFilmId((short) 1);
        movie.setTitle("Inception");
        Language language = new Language("English");
        movie.setLanguage(language);
        movie.setCategory(List.of(new Category("Action")));
        movie.setActors(List.of(new Actor("Leonardo", "DiCaprio")));

        when(movieRepository.findAll()).thenReturn(List.of(movie));

        List<MovieDto> movies = adminService.getAllMovies();

        assertNotNull(movies);
        assertEquals(1, movies.size());
        assertEquals("Inception", movies.get(0).getTitle());
    }


    // Test for addMovie()
    @Test
    public void testAddMovie() {
        MovieDto movieDto = new MovieDto();
        movieDto.setTitle("Inception");
        movieDto.setDescription("A mind-bending thriller");
        movieDto.setLanguage("English");
        movieDto.setCategory(List.of("Action"));
        movieDto.setActors(List.of("Leonardo DiCaprio"));

        Language language = new Language("English");
        Category category = new Category("Action");
        Actor actor = new Actor("Leonardo", "DiCaprio");

        // Mock language, category, and actor repositories
        when(languageRepository.findByName("English")).thenReturn(Optional.of(language));
        when(categoryRepository.findByName("Action")).thenReturn(Optional.of(category));
        when(actorRepository.findByFirstNameAndLastName("Leonardo", "DiCaprio")).thenReturn(Optional.of(actor));

        Movie savedMovie = new Movie();
        savedMovie.setFilmId((short) 1);
        savedMovie.setTitle("Inception");

        // Mock movie repository save method
        when(movieRepository.save(any(Movie.class))).thenReturn(savedMovie);

        Movie result = adminService.addMovie(movieDto);

        assertNotNull(result);
        assertEquals("Inception", result.getTitle());
    }


    // Test for deleteMovie()
    @Test
    public void testDeleteMovie() {
        Movie movie = new Movie();
        movie.setFilmId((short) 1);
        movie.setTitle("Inception");

        when(movieRepository.findById((short) 1)).thenReturn(Optional.of(movie));

        adminService.deleteMovie((short) 1);

        verify(movieRepository, times(1)).delete(movie);
    }

    // Test for getAllLanguages()
    @Test
    public void testGetAllLanguages() {
        Language language = new Language("English");
        when(languageRepository.findAll()).thenReturn(List.of(language));

        List<LanguageDto> languages = adminService.getAllLanguages();

        assertNotNull(languages);
        assertEquals(1, languages.size());
        assertEquals("English", languages.get(0).getName());
    }

    // Test for addLanguage()
    @Test
    public void testAddLanguage() {
        LanguageDto languageDto = new LanguageDto(null, "English");

        // Mock that the language does not exist yet
        when(languageRepository.existsByName("English")).thenReturn(false);

        // Mock the saved Language object
        Language savedLanguage = new Language();
        savedLanguage.setLanguageId((byte) 1);
        savedLanguage.setName("English");

        // Mock the save method to return the savedLanguage object
        when(languageRepository.save(any(Language.class))).thenReturn(savedLanguage);

        // Call the service method
        LanguageDto result = adminService.addLanguage(languageDto);

        // Assert the result
        assertNotNull(result);
        assertEquals("English", result.getName());
        assertEquals((byte) 1, result.getId()); // Ensure the languageId is set
    }


    // Test for deleteLanguage()
    @Test
    public void testDeleteLanguage() {
        Language language = new Language("English");

        when(languageRepository.findById((short) 1)).thenReturn(Optional.of(language));
        when(movieRepository.existsByLanguage(language)).thenReturn(false);

        adminService.deleteLanguage((short) 1);

        verify(languageRepository, times(1)).delete(language);
    }

    // Test for getAllCategories()
    @Test
    public void testGetAllCategories() {
        Category category = new Category("Action");
        when(categoryRepository.findAll()).thenReturn(List.of(category));

        List<CategoryDto> categories = adminService.getAllCategories();

        assertNotNull(categories);
        assertEquals(1, categories.size());
        assertEquals("Action", categories.get(0).getName());
    }

    // Test for addCategory()
    @Test
    public void testAddCategory() {
        CategoryDto categoryDto = new CategoryDto(null, "Action");

        when(categoryRepository.existsByName("Action")).thenReturn(false);

        Category savedCategory = new Category();
        savedCategory.setCategoryId((byte) 1);
        savedCategory.setName("Action");

        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

        // Act: Call the service method
        CategoryDto result = adminService.addCategory(categoryDto);

        // Assert: Check the result
        assertNotNull(result);
        assertEquals("Action", result.getName());
        assertEquals((byte) 1, result.getId());

        // Verify that save() was called once
        verify(categoryRepository, times(1)).save(any(Category.class));
    }


    // Test for deleteCategory()
    @Test
    public void testDeleteCategory() {
        Category category = new Category("Action");

        when(categoryRepository.findById((short) 1)).thenReturn(Optional.of(category));
        when(movieRepository.existsByCategory(category)).thenReturn(false);

        adminService.deleteCategory((short) 1);

        verify(categoryRepository, times(1)).delete(category);
    }

    // Test for getAllActors()
    @Test
    public void testGetAllActors() {
        Actor actor = new Actor("Leonardo", "DiCaprio");
        when(actorRepository.findAll()).thenReturn(List.of(actor));

        List<ActorDto> actors = adminService.getAllActors();

        assertNotNull(actors);
        assertEquals(1, actors.size());
        assertEquals("Leonardo DiCaprio", actors.get(0).getFirstName() + " " + actors.get(0).getLastName());
    }

    // Test for addActor()
    @Test
    public void testAddActor() {
        ActorDto actorDto = new ActorDto(null, "Leonardo", "DiCaprio");

        // Mock saved Actor
        Actor savedActor = new Actor("Leonardo", "DiCaprio");
        savedActor.setActorId((short) 1);

        // Mock the save method
        when(actorRepository.save(any(Actor.class))).thenReturn(savedActor);

        ActorDto result = adminService.addActor(actorDto);

        assertNotNull(result);
        assertEquals("Leonardo", result.getFirstName());
        assertEquals("DiCaprio", result.getLastName());
        assertEquals((short) 1, result.getId());
    }


    // Test for deleteActor()
    @Test
    public void testDeleteActor() {
        Actor actor = new Actor("Leonardo", "DiCaprio");

        when(actorRepository.findById((short) 1)).thenReturn(Optional.of(actor));
        when(movieRepository.existsByActorsContaining(actor)).thenReturn(false);

        adminService.deleteActor((short) 1);

        verify(actorRepository, times(1)).delete(actor);
    }
}
