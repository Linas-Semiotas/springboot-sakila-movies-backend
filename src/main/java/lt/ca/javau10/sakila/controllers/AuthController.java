package lt.ca.javau10.sakila.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lt.ca.javau10.sakila.models.dto.LoginDto;
import lt.ca.javau10.sakila.models.dto.RegisterDto;
import lt.ca.javau10.sakila.security.responses.JwtResponse;
import lt.ca.javau10.sakila.security.responses.MessageResponse;
import lt.ca.javau10.sakila.services.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	Logger logger = LogManager.getLogger(AuthController.class);
	
	private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }
	
    @PostMapping("/register")
    public ResponseEntity<MessageResponse> registerUser(@RequestBody RegisterDto registerDto) {
        service.register(registerDto);
        return ResponseEntity.ok(new MessageResponse("User created successfully"));
    }
    
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> createAuthenticationToken(@RequestBody LoginDto loginDto) {
        JwtResponse jwtResponse = service.login(loginDto);
        return ResponseEntity.ok(jwtResponse);
    }
}
