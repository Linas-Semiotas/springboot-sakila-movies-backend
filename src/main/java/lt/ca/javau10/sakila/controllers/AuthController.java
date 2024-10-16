package lt.ca.javau10.sakila.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import lt.ca.javau10.sakila.utils.Utils;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
	
	private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }
	
    //REGISTER
    
    @PostMapping("/register")
    public ResponseEntity<MessageResponse> registerUser(@RequestBody RegisterDto registerDto) {
        service.register(registerDto);
        Utils.infoAuth(logger, "New user registered successfully: {}", registerDto.getUsername());
        return ResponseEntity.ok(new MessageResponse("User created successfully"));
    }
    
    //LOGIN
    
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> createAuthenticationToken(@RequestBody LoginDto loginDto) {
    	Utils.infoAuth(logger, "User attempting to log in: {}", loginDto.getUsername());
        JwtResponse jwtResponse = service.login(loginDto);
        Utils.infoAuth(logger, "User logged in successfully: {}", loginDto.getUsername());
        return ResponseEntity.ok(jwtResponse);
    }
}
