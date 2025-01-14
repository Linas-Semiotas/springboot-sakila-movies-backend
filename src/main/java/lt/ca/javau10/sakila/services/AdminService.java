package lt.ca.javau10.sakila.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import lt.ca.javau10.sakila.exceptions.ResourceNotFoundException;
import lt.ca.javau10.sakila.models.Actor;
import lt.ca.javau10.sakila.models.Category;
import lt.ca.javau10.sakila.models.Customer;
import lt.ca.javau10.sakila.models.Language;
import lt.ca.javau10.sakila.models.Movie;
import lt.ca.javau10.sakila.models.User;
import lt.ca.javau10.sakila.models.dto.ActorDto;
import lt.ca.javau10.sakila.models.dto.AdminUserDto;
import lt.ca.javau10.sakila.models.dto.CategoryDto;
import lt.ca.javau10.sakila.models.dto.LanguageDto;
import lt.ca.javau10.sakila.models.dto.MovieDto;
import lt.ca.javau10.sakila.repositories.ActorRepository;
import lt.ca.javau10.sakila.repositories.CategoryRepository;
import lt.ca.javau10.sakila.repositories.CustomerRepository;
import lt.ca.javau10.sakila.repositories.LanguageRepository;
import lt.ca.javau10.sakila.repositories.MovieRepository;
import lt.ca.javau10.sakila.repositories.UserRepository;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final LanguageRepository languageRepository;
    private final CategoryRepository categoryRepository;
    private final ActorRepository actorRepository;
    private final MovieRepository movieRepository;

    public AdminService(UserRepository userRepository, 
    		CustomerRepository customerRepository, 
    		LanguageRepository languageRepository, 
    		CategoryRepository categoryRepository,
    		ActorRepository actorRepository,
    		MovieRepository movieRepository) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.languageRepository = languageRepository;
        this.categoryRepository = categoryRepository;
        this.actorRepository = actorRepository;
        this.movieRepository = movieRepository;
    }
    
    //USERS
    
    @Transactional(readOnly = true)
    public List<AdminUserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<AdminUserDto> userDtos = new ArrayList<>();
        for (User user : users) {
            AdminUserDto dto = new AdminUserDto();
            dto.setUserId(user.getUserId());
            dto.setUsername(user.getUsername());
            dto.setUserRole(user.getRoles().contains("USER"));
            dto.setAdminRole(user.getRoles().contains("ADMIN"));
            dto.setEnabled(user.getCustomer().getActive() == 1);
            userDtos.add(dto);
        }
        return userDtos;
    }

    @Transactional
    public void updateUser(int userId, AdminUserDto updateDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Customer customer = user.getCustomer();

        customer.setActive(updateDto.isEnabled() ? (byte) 1 : (byte) 0);
        customerRepository.save(customer);

        updateRole(user, "USER", updateDto.isUserRole());
        updateRole(user, "ADMIN", updateDto.isAdminRole());

        userRepository.save(user);
    }

    private void updateRole(User user, String roleName, boolean addRole) {
        Set<String> roles = user.getRoles();
        if (addRole) {
            roles.add(roleName);
        } else {
            roles.remove(roleName);
        }
        user.setRoles(roles);
    }

    //MOVIES
    
    @Transactional(readOnly = true)
    public List<MovieDto> getAllMovies() {    	
        return movieRepository.findAll()
                .stream()
                .map(movie -> new MovieDto(
                    movie.getFilmId(), 
                    movie.getTitle(), 
                    movie.getDescription(), 
                    movie.getReleaseYear(), 
                    movie.getFilmLength(), 
                    movie.getRating(), 
                    movie.getLanguage().getName(),
                    movie.getCategory().stream()
                    	.map(categ -> categ.getName())
                    	.collect(Collectors.toList()),
                    movie.getSpecialFeatures(),
                    movie.getRentalRate(),
                    movie.getReplacementCost(),
                    movie.getRentalDuration(),
                    movie.getActor().stream()
                         .map(actor -> actor.getFirstName() + " " + actor.getLastName())
                         .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }
    
    @Transactional
    public Movie addMovie(MovieDto movieDto) {
        // Fetch or create language
        Language language = languageRepository.findByName(movieDto.getLanguage())
                .orElseThrow(() -> new IllegalArgumentException("Invalid language " + movieDto.getLanguage()));
  
        // Create and save the new movie
        Movie movie = new Movie();
        movie.setTitle(movieDto.getTitle());
        movie.setDescription(movieDto.getDescription());
        movie.setReleaseYear(movieDto.getReleaseYear());
        movie.setFilmLength(movieDto.getFilmLength());
        movie.setRating(movieDto.getRating());
        movie.setLanguage(language);
        movie.setSpecialFeatures(movieDto.getSpecialFeatures());
        movie.setRentalRate(movieDto.getRentalRate());
        movie.setReplacementCost(movieDto.getReplacementCost());
        movie.setRentalDuration(movieDto.getRentalDuration());
        
        List<Category> categories = movieDto.getCategory().stream()
            .map(categoryName -> categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category: " + categoryName)))
            .collect(Collectors.toList());
        movie.setCategory(categories);
       
        List<Actor> actors = movieDto.getActors().stream()
                .map(actorName -> {
                    String[] nameParts = actorName.split(" ");
                    if (nameParts.length != 2) {
                        throw new IllegalArgumentException("Invalid actor name format: " + actorName);
                    }
                    return actorRepository.findByFirstNameAndLastName(nameParts[0], nameParts[1])
                        .orElseThrow(() -> new IllegalArgumentException("Invalid actor: " + actorName));
                })
                .collect(Collectors.toList());

            movie.setActors(actors);
        
        return movieRepository.save(movie);
    }
    
    @Transactional
    public void updateMovie(Short movieId, MovieDto movieDto) {
        Movie existingMovie = movieRepository.findById(movieId)
            .orElseThrow(() -> new IllegalArgumentException("Movie with id " + movieId + " not found"));

        existingMovie.setTitle(movieDto.getTitle());
        existingMovie.setDescription(movieDto.getDescription());
        existingMovie.setReleaseYear(movieDto.getReleaseYear());
        existingMovie.setFilmLength(movieDto.getFilmLength());
        existingMovie.setRating(movieDto.getRating());
        existingMovie.setSpecialFeatures(movieDto.getSpecialFeatures());
        existingMovie.setRentalRate(movieDto.getRentalRate());
        existingMovie.setReplacementCost(movieDto.getReplacementCost());
        existingMovie.setRentalDuration(movieDto.getRentalDuration());

        Language language = languageRepository.findByName(movieDto.getLanguage())
            .orElseThrow(() -> new IllegalArgumentException("Invalid language: " + movieDto.getLanguage()));
        existingMovie.setLanguage(language);

        List<Category> categories = movieDto.getCategory().stream()
            .map(categoryName -> categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category: " + categoryName)))
            .collect(Collectors.toList());
        existingMovie.setCategory(categories);

        List<Actor> actors = movieDto.getActors().stream()
            .map(actorName -> {
                String[] nameParts = actorName.split(" ");
                if (nameParts.length != 2) {
                    throw new IllegalArgumentException("Invalid actor name format: " + actorName);
                }
                return actorRepository.findByFirstNameAndLastName(nameParts[0], nameParts[1])
                    .orElseThrow(() -> new IllegalArgumentException("Invalid actor: " + actorName));
            })
            .collect(Collectors.toList());
        existingMovie.setActors(actors);

        movieRepository.save(existingMovie);
    }
    
    @Transactional
    public void deleteMovie(Short movieId) {
        Movie movie = movieRepository.findById(movieId)
            .orElseThrow(() -> new IllegalArgumentException("Movie with id " + movieId + " not found"));

        movieRepository.delete(movie);
    }
    
    //LANGUAGES
    
    @Transactional(readOnly = true)
    public List<LanguageDto> getAllLanguages() {
        return languageRepository.findAll()
                .stream()
                .map(language -> new LanguageDto(language.getLanguageId(), language.getName()))
                .collect(Collectors.toList());
    }

    @Transactional
    public LanguageDto addLanguage(LanguageDto languageDto) {
        if (languageRepository.existsByName(languageDto.getName())) {
            throw new IllegalArgumentException("Language already exists");
        }
        Language language = new Language(languageDto.getName());
        Language savedLanguage = languageRepository.save(language);
        return new LanguageDto(savedLanguage.getLanguageId(), savedLanguage.getName());
    }

    @Transactional
    public void deleteLanguage(Short id) {
        Language language = languageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Language not found"));
        if (movieRepository.existsByLanguage(language)) {
            throw new IllegalArgumentException("Cannot delete language because it is used by one or more movies.");
        }
        languageRepository.delete(language);
    }
    
    //CATEGORIES
    
    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(category -> new CategoryDto(category.getCategoryId(), category.getName()))
                .collect(Collectors.toList());
    }

    @Transactional
    public CategoryDto addCategory(CategoryDto categoryDto) {
        if (categoryRepository.existsByName(categoryDto.getName())) {
            throw new IllegalArgumentException("Category already exists");
        }
        Category category = new Category(categoryDto.getName());
        Category savedCategory = categoryRepository.save(category);
        return new CategoryDto(savedCategory.getCategoryId(), savedCategory.getName());
    }

    @Transactional
    public void deleteCategory(Short id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        if (movieRepository.existsByCategory(category)) {
            throw new IllegalArgumentException("Cannot delete category because it is associated with one or more movies.");
        }
        categoryRepository.delete(category);
    }
    
    //ACTORS
    
    @Transactional(readOnly = true)
    public List<ActorDto> getAllActors() {
        return actorRepository.findAll()
                .stream()
                .map(actor -> new ActorDto(actor.getActorId(), actor.getFirstName(), actor.getLastName()))
                .collect(Collectors.toList());
    }

    @Transactional
    public ActorDto addActor(ActorDto actorDto) {
        Actor actor = new Actor(actorDto.getFirstName(), actorDto.getLastName());
        Actor savedActor = actorRepository.save(actor);
        return new ActorDto(savedActor.getActorId(), savedActor.getFirstName(), savedActor.getLastName());
    }

    @Transactional
    public void deleteActor(Short id) {
        Actor actor = actorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Actor not found"));
        if (movieRepository.existsByActorsContaining(actor)) {
            throw new IllegalArgumentException("Cannot delete actor because they are associated with one or more movies.");
        }
        actorRepository.delete(actor);
    }
}