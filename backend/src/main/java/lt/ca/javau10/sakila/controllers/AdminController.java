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
import lt.ca.javau10.sakila.models.Language;
import lt.ca.javau10.sakila.models.dto.AdminUserDto;
import lt.ca.javau10.sakila.models.dto.LanguageDto;
import lt.ca.javau10.sakila.services.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService service;

    public AdminController(AdminService service) {
        this.service = service;
    }

    //ADMIN -> USERS
    
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
    
    //ADMIN -> MOVIES
    
    //ADMIN -> MOVIES -> LANGUAGES
    
    @GetMapping("/languages")
    public List<Language> getAllLanguages() {
        return service.getAllLanguages();
    }

    @PostMapping("/languages")
    public ResponseEntity<String> addLanguage(@RequestBody LanguageDto languageDto) {
        try {
            service.addLanguage(languageDto.getName());
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
}
