package lt.ca.javau10.sakila.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lt.ca.javau10.sakila.exceptions.ResourceNotFoundException;
import lt.ca.javau10.sakila.models.Actor;
import lt.ca.javau10.sakila.models.Category;
import lt.ca.javau10.sakila.models.Customer;
import lt.ca.javau10.sakila.models.Language;
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

    public void updateUser(int userId, AdminUserDto updateDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Customer customer = user.getCustomer();

        // Update enabled status
        customer.setActive(updateDto.isEnabled() ? (byte) 1 : (byte) 0);
        customerRepository.save(customer);

        // Update roles
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
                    movie.getCategory().getName(),
                    movie.getSpecialFeatures(),
                    movie.getRentalRate(),
                    movie.getReplacementCost(),
                    movie.getRentalDuration(),
                    movie.getActor().stream()
                         .map(actor -> actor.getFirstName() + " " + actor.getLastName()) // Map actor details
                         .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }
    
    //LANGUAGES
    
    public List<LanguageDto> getAllLanguages() {
        return languageRepository.findAll()
                .stream()
                .map(language -> new LanguageDto(language.getLanguageId(), language.getName()))
                .collect(Collectors.toList());
    }

    public LanguageDto addLanguage(LanguageDto languageDto) {
        if (languageRepository.existsByName(languageDto.getName())) {
            throw new IllegalArgumentException("Language already exists");
        }
        Language language = new Language(languageDto.getName());
        Language savedLanguage = languageRepository.save(language);
        return new LanguageDto(savedLanguage.getLanguageId(), savedLanguage.getName());
    }

    public void deleteLanguage(Short id) {
        Language language = languageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Language not found"));
        if (movieRepository.existsByLanguage(language)) {
            throw new IllegalArgumentException("Cannot delete language because it is used by one or more movies.");
        }
        languageRepository.delete(language);
    }
    
    //CATEGORIES
    
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(category -> new CategoryDto(category.getCategoryId(), category.getName()))
                .collect(Collectors.toList());
    }

    public CategoryDto addCategory(CategoryDto categoryDto) {
        if (categoryRepository.existsByName(categoryDto.getName())) {
            throw new IllegalArgumentException("Category already exists");
        }
        Category category = new Category(categoryDto.getName());
        Category savedCategory = categoryRepository.save(category);
        return new CategoryDto(savedCategory.getCategoryId(), savedCategory.getName());
    }

    public void deleteCategory(Short id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        if (movieRepository.existsByCategory(category)) {
            throw new IllegalArgumentException("Cannot delete category because it is associated with one or more movies.");
        }
        categoryRepository.delete(category);
    }
    
    //ACTORS
    
    public List<ActorDto> getAllActors() {
        return actorRepository.findAll()
                .stream()
                .map(actor -> new ActorDto(actor.getActorId(), actor.getFirstName(), actor.getLastName()))
                .collect(Collectors.toList());
    }

    public ActorDto addActor(ActorDto actorDto) {
        Actor actor = new Actor(actorDto.getFirstName(), actorDto.getLastName());
        Actor savedActor = actorRepository.save(actor);
        return new ActorDto(savedActor.getActorId(), savedActor.getFirstName(), savedActor.getLastName());
    }

    public void deleteActor(Short id) {
        Actor actor = actorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Actor not found"));
        if (movieRepository.existsByActorsContaining(actor)) {
            throw new IllegalArgumentException("Cannot delete actor because they are associated with one or more movies.");
        }
        actorRepository.delete(actor);
    }
}