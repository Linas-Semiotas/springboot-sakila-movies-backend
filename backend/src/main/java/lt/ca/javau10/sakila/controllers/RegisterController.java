package lt.ca.javau10.sakila.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lt.ca.javau10.sakila.dto.RegisterDto;
import lt.ca.javau10.sakila.services.RegisterService;

@RestController
@RequestMapping("/register")
@CrossOrigin(origins = "http://localhost:3000")
public class RegisterController {
	@Autowired
    private RegisterService service;
	
    @PostMapping
    public ResponseEntity<String> registerUser(@RequestBody RegisterDto registerDto) {
        try {
            service.register(registerDto);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering user");
        }
    }
}
