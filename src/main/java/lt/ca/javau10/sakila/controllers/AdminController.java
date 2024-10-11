package lt.ca.javau10.sakila.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.EntityNotFoundException;
import lt.ca.javau10.sakila.exceptions.ResourceNotFoundException;
import lt.ca.javau10.sakila.models.dto.ActorDto;
import lt.ca.javau10.sakila.models.dto.AdminUserDto;
import lt.ca.javau10.sakila.models.dto.CategoryDto;
import lt.ca.javau10.sakila.models.dto.LanguageDto;
import lt.ca.javau10.sakila.models.dto.MovieDto;
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
    public ResponseEntity<String> updateUser(@PathVariable int userId, @RequestBody AdminUserDto updateDto) {
        try {
        	service.updateUser(userId, updateDto);
            return ResponseEntity.ok("User updated successfully");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating user");
        }
    }

    //MOVIES
    
    @GetMapping("movies")
    public ResponseEntity<List<MovieDto>> getAllMovies() {
        List<MovieDto> movies = service.getAllMovies();
        return ResponseEntity.ok(movies);
    }
    
    //LANGUAGES
    
    @GetMapping("/languages")
    public ResponseEntity<List<LanguageDto>> getAllLanguages() {
        List<LanguageDto> languages = service.getAllLanguages();
        return ResponseEntity.ok(languages);
    }

    @PostMapping("/languages")
    public ResponseEntity<String> addLanguage(@RequestBody LanguageDto languageDto) {
        try {
            service.addLanguage(languageDto);
            return ResponseEntity.ok("Language added successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/languages/{id}")
    public ResponseEntity<String> deleteLanguage(@PathVariable Short id) {
        try {
            service.deleteLanguage(id);
            return ResponseEntity.ok("Language deleted successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
    //CATEGORIES
    
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = service.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PostMapping("/categories")
    public ResponseEntity<String> addCategory(@RequestBody CategoryDto categoryDto) {
        try {
        	service.addCategory(categoryDto);
            return ResponseEntity.ok("Category added successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Short id) {
        try {
        	service.deleteCategory(id);
            return ResponseEntity.ok("Category deleted successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
    //ACTORS
    
    @GetMapping("/actors")
    public ResponseEntity<List<ActorDto>> getAllActors() {
        List<ActorDto> actors = service.getAllActors();
        return ResponseEntity.ok(actors);
    }

    @PostMapping("/actors")
    public ResponseEntity<String> addActor(@RequestBody ActorDto actorDto) {
        try {
        	service.addActor(actorDto);
            return ResponseEntity.ok("Actor added successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/actors/{id}")
    public ResponseEntity<String> deleteActor(@PathVariable Short id) {
        try {
        	service.deleteActor(id);
            return ResponseEntity.ok("Actor deleted successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
