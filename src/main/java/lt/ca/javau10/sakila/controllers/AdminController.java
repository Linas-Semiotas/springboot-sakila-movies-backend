package lt.ca.javau10.sakila.controllers;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lt.ca.javau10.sakila.models.dto.ActorDto;
import lt.ca.javau10.sakila.models.dto.AdminUserDto;
import lt.ca.javau10.sakila.models.dto.CategoryDto;
import lt.ca.javau10.sakila.models.dto.LanguageDto;
import lt.ca.javau10.sakila.models.dto.MovieDto;
import lt.ca.javau10.sakila.security.responses.MessageResponse;
import lt.ca.javau10.sakila.services.AdminService;
import lt.ca.javau10.sakila.utils.Utils;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
	
    private final AdminService service;

    public AdminController(AdminService service) {
        this.service = service;
    }

    //USERS
    
    @GetMapping("/users")
    public ResponseEntity<List<AdminUserDto>> getAllUsers() {
        List<AdminUserDto> users = service.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<MessageResponse> updateUser(@PathVariable int userId, @RequestBody AdminUserDto updateDto, Principal principal) {
    	String username = principal.getName();
        service.updateUser(userId, updateDto);
        Utils.infoAdmin(logger, "Admin {} updated user with ID {}", username, userId);
        return ResponseEntity.ok(new MessageResponse("User updated successfully"));
    }

    //MOVIES
    
    @GetMapping("/movies")
    public ResponseEntity<List<MovieDto>> getAllMovies() {
        List<MovieDto> movies = service.getAllMovies();
        return ResponseEntity.ok(movies);
    }

    @PostMapping("/movies")
    public ResponseEntity<MessageResponse> addMovie(@Valid @RequestBody MovieDto movieDto, Principal principal) {
    	String username = principal.getName();
        service.addMovie(movieDto);
        Utils.infoAdmin(logger, "Admin {} added movie: {}", username, movieDto.getTitle());
        return ResponseEntity.ok(new MessageResponse("Movie added successfully"));
    }

    @PutMapping("/movies/{id}")
    public ResponseEntity<MessageResponse> updateMovie(@Valid @PathVariable Short id, @RequestBody MovieDto movieDto, Principal principal) {
    	String username = principal.getName();
        service.updateMovie(id, movieDto);
        Utils.infoAdmin(logger, "Admin {} updated movie with ID {}", username, id);
        return ResponseEntity.ok(new MessageResponse("Movie updated successfully"));
    }

    @DeleteMapping("/movies/{id}")
    public ResponseEntity<MessageResponse> deleteMovie(@PathVariable Short id, Principal principal) {
    	String username = principal.getName();
        service.deleteMovie(id);
        Utils.infoAdmin(logger, "Admin {} deleted movie with ID {}", username, id);
        return ResponseEntity.ok(new MessageResponse("Movie deleted successfully"));
    }
    
    //LANGUAGES
    
    @GetMapping("/languages")
    public ResponseEntity<List<LanguageDto>> getAllLanguages() {
        List<LanguageDto> languages = service.getAllLanguages();
        return ResponseEntity.ok(languages);
    }

    @PostMapping("/languages")
    public ResponseEntity<MessageResponse> addLanguage(@Valid @RequestBody LanguageDto languageDto, Principal principal) {
    	String username = principal.getName();
        service.addLanguage(languageDto);
        Utils.infoAdmin(logger, "Admin {} added language: {}", username, languageDto.getName());
        return ResponseEntity.ok(new MessageResponse("Language added successfully"));
    }

    @DeleteMapping("/languages/{id}")
    public ResponseEntity<MessageResponse> deleteLanguage(@PathVariable Short id, Principal principal) {
    	String username = principal.getName();
        service.deleteLanguage(id);
        Utils.infoAdmin(logger, "Admin {} deleted language with ID {}", username, id);
        return ResponseEntity.ok(new MessageResponse("Language deleted successfully"));
    }
    
    //CATEGORIES
    
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = service.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PostMapping("/categories")
    public ResponseEntity<MessageResponse> addCategory(@Valid @RequestBody CategoryDto categoryDto, Principal principal) {
    	String username = principal.getName();
        service.addCategory(categoryDto);
        Utils.infoAdmin(logger, "Admin {} added category: {}", username, categoryDto.getName());
        return ResponseEntity.ok(new MessageResponse("Category added successfully"));
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<MessageResponse> deleteCategory(@PathVariable Short id, Principal principal) {
    	String username = principal.getName();
        service.deleteCategory(id);
        Utils.infoAdmin(logger, "Admin {} deleted category with ID {}", username, id);
        return ResponseEntity.ok(new MessageResponse("Category deleted successfully"));
    }
    
    //ACTORS
    
    @GetMapping("/actors")
    public ResponseEntity<List<ActorDto>> getAllActors() {
        List<ActorDto> actors = service.getAllActors();
        return ResponseEntity.ok(actors);
    }

    @PostMapping("/actors")
    public ResponseEntity<MessageResponse> addActor(@Valid @RequestBody ActorDto actorDto, Principal principal) {
    	String username = principal.getName();
        service.addActor(actorDto);
        Utils.infoAdmin(logger, "Admin {} added actor: {} {}", username, actorDto.getFirstName(), actorDto.getLastName());
        return ResponseEntity.ok(new MessageResponse("Actor added successfully"));
    }

    @DeleteMapping("/actors/{id}")
    public ResponseEntity<MessageResponse> deleteActor(@PathVariable Short id, Principal principal) {
    	String username = principal.getName();
        service.deleteActor(id);
        Utils.infoAdmin(logger, "Admin {} deleted actor with ID {}", username, id);
        return ResponseEntity.ok(new MessageResponse("Actor deleted successfully"));
    }
}
