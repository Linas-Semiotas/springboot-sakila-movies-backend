package lt.ca.javau10.sakila.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lt.ca.javau10.sakila.models.dto.ActorDto;
import lt.ca.javau10.sakila.models.dto.AdminUserDto;
import lt.ca.javau10.sakila.models.dto.CategoryDto;
import lt.ca.javau10.sakila.models.dto.LanguageDto;
import lt.ca.javau10.sakila.models.dto.MovieDto;
import lt.ca.javau10.sakila.security.responses.MessageResponse;
import lt.ca.javau10.sakila.services.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

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
    public ResponseEntity<MessageResponse> updateUser(@PathVariable int userId, @RequestBody AdminUserDto updateDto) {
        service.updateUser(userId, updateDto);
        return ResponseEntity.ok(new MessageResponse("User updated successfully"));
    }

    //MOVIES
    
    @GetMapping("/movies")
    public ResponseEntity<List<MovieDto>> getAllMovies() {
        List<MovieDto> movies = service.getAllMovies();
        return ResponseEntity.ok(movies);
    }

    @PostMapping("/movies")
    public ResponseEntity<MessageResponse> addMovie(@RequestBody MovieDto movieDto) {
        service.addMovie(movieDto);
        return ResponseEntity.ok(new MessageResponse("Movie added successfully"));
    }

    @PutMapping("/movies/{id}")
    public ResponseEntity<MessageResponse> updateMovie(@PathVariable Short id, @RequestBody MovieDto movieDto) {
        service.updateMovie(id, movieDto);
        return ResponseEntity.ok(new MessageResponse("Movie updated successfully"));
    }

    @DeleteMapping("/movies/{id}")
    public ResponseEntity<MessageResponse> deleteMovie(@PathVariable Short id) {
        service.deleteMovie(id);
        return ResponseEntity.ok(new MessageResponse("Movie deleted successfully"));
    }
    
    //LANGUAGES
    
    @GetMapping("/languages")
    public ResponseEntity<List<LanguageDto>> getAllLanguages() {
        List<LanguageDto> languages = service.getAllLanguages();
        return ResponseEntity.ok(languages);
    }

    @PostMapping("/languages")
    public ResponseEntity<MessageResponse> addLanguage(@RequestBody LanguageDto languageDto) {
        service.addLanguage(languageDto);
        return ResponseEntity.ok(new MessageResponse("Language added successfully"));
    }

    @DeleteMapping("/languages/{id}")
    public ResponseEntity<MessageResponse> deleteLanguage(@PathVariable Short id) {
        service.deleteLanguage(id);
        return ResponseEntity.ok(new MessageResponse("Language deleted successfully"));
    }
    
    //CATEGORIES
    
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = service.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PostMapping("/categories")
    public ResponseEntity<MessageResponse> addCategory(@RequestBody CategoryDto categoryDto) {
        service.addCategory(categoryDto);
        return ResponseEntity.ok(new MessageResponse("Category added successfully"));
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<MessageResponse> deleteCategory(@PathVariable Short id) {
        service.deleteCategory(id);
        return ResponseEntity.ok(new MessageResponse("Category deleted successfully"));
    }
    
    //ACTORS
    
    @GetMapping("/actors")
    public ResponseEntity<List<ActorDto>> getAllActors() {
        List<ActorDto> actors = service.getAllActors();
        return ResponseEntity.ok(actors);
    }

    @PostMapping("/actors")
    public ResponseEntity<MessageResponse> addActor(@RequestBody ActorDto actorDto) {
        service.addActor(actorDto);
        return ResponseEntity.ok(new MessageResponse("Actor added successfully"));
    }

    @DeleteMapping("/actors/{id}")
    public ResponseEntity<MessageResponse> deleteActor(@PathVariable Short id) {
        service.deleteActor(id);
        return ResponseEntity.ok(new MessageResponse("Actor deleted successfully"));
    }
}
