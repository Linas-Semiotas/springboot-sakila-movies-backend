package lt.ca.javau10.sakila.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lt.ca.javau10.sakila.models.dto.LoginDto;
import lt.ca.javau10.sakila.models.dto.RegisterDto;
import lt.ca.javau10.sakila.security.responses.JwtResponse;
import lt.ca.javau10.sakila.security.responses.MessageResponse;
import lt.ca.javau10.sakila.security.utils.JwtUtil;
import lt.ca.javau10.sakila.services.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	Logger logger = LogManager.getLogger(AuthController.class);
	
	@Autowired
    private AuthService service;
	
	@Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;
	
    @PostMapping("/register")
    public ResponseEntity<MessageResponse> registerUser(@RequestBody RegisterDto registerDto) {
        try {
            service.register(registerDto);
            logger.info("User registered successfully: " + registerDto.getUsername());
            return ResponseEntity.ok(new MessageResponse("User registered successfully"));
        } catch (Exception e) {
        	logger.error("Error registering user: " + registerDto.getUsername(), e);
        	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error registering user"));
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginDto loginDto) throws Exception {    	
    	try {
    		// Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
            );
            // Set authentication to the security context
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // Extract UserDetails from the Authentication object
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            // Generate the JWT token using the UserDetails
            String jwt = jwtUtil.generateToken(userDetails);
            // Get user roles
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            logger.info("User logged in successfully: " + loginDto.getUsername());
            return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), null, roles));
        } catch (BadCredentialsException e) {
            logger.warn("Invalid login attempt: " + loginDto.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Invalid login credentials"));
        } catch (DisabledException e) {
            logger.warn("Disabled account login attempt: " + loginDto.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("User account is disabled"));
        } catch (Exception e) {
            logger.error("Error during login: " + loginDto.getUsername(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResponse("Error logging in"));
        }
    }
}
