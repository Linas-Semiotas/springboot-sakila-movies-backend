package lt.ca.javau10.sakila.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
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
    
    // Inject the property for cookie security
    @Value("${cookie.secure}")
    private boolean cookieSecure;

    // Inject the JWT expiration time in milliseconds
    @Value("${jwtExpirationMs}")
    private long jwtExpirationMs;
	
    //REGISTER
    
    @PostMapping("/register")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody RegisterDto registerDto) {
        service.register(registerDto);
        Utils.infoAuth(logger, "New user registered successfully: {}", registerDto.getUsername());
        return ResponseEntity.ok(new MessageResponse("User created successfully"));
    }
    
    //LOGIN
    
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> createAuthenticationToken(@Valid @RequestBody LoginDto loginDto, HttpServletResponse response) {
    	Utils.infoAuth(logger, "User attempting to log in: {}", loginDto.getUsername());
        JwtResponse jwtResponse = service.login(loginDto);
        
        Cookie cookie = new Cookie("token", jwtResponse.getToken());
        cookie.setHttpOnly(true); // Prevents JavaScript access
        cookie.setSecure(cookieSecure); // Set true for HTTPS, false for local testing
        cookie.setPath("/");
        cookie.setMaxAge((int) (jwtExpirationMs / 1000)); // Time is set in application.properties
        response.addCookie(cookie); // Use the response object to add the cookie
        
        Utils.infoAuth(logger, "User logged in successfully: {}", loginDto.getUsername());
        return ResponseEntity.ok(jwtResponse);
    }
    
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // Create a cookie with a null value and set Max-Age to 0 to delete it
        Cookie cookie = new Cookie("token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(cookieSecure); // Set to true if using HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(0); // Immediately delete the cookie
        response.addCookie(cookie);

        return ResponseEntity.ok("Logout successful");
    }
    
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        return service.getCurrentUser();
    }

    // Refresh the token before it expires
    @PostMapping("/refresh-token")
    public ResponseEntity<JwtResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Call the refreshToken method in the service
        JwtResponse newToken = service.refreshToken(authentication);

        // Set the new token in the HttpOnly cookie
        Cookie cookie = new Cookie("token", newToken.getToken());
        cookie.setHttpOnly(true); 
        cookie.setPath("/");
        cookie.setMaxAge((int) (jwtExpirationMs / 1000));  // Example: set it to 1 hour
        response.addCookie(cookie);

        return ResponseEntity.ok(newToken);
    }
}
