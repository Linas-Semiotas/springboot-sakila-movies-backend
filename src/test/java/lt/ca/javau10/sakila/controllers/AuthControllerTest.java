package lt.ca.javau10.sakila.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lt.ca.javau10.sakila.models.dto.LoginDto;
import lt.ca.javau10.sakila.models.dto.RegisterDto;
import lt.ca.javau10.sakila.security.responses.JwtResponse;
import lt.ca.javau10.sakila.security.utils.JwtUtil;
import lt.ca.javau10.sakila.services.AuthService;
import lt.ca.javau10.sakila.services.CustomUserDetailsService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable security filters for non-security testing
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;
    
    @MockBean
    private AuthenticationManager authenticationManager;
    
    @MockBean
    private JwtUtil jwtUtil;
    
    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    // Test for registering a user with updated RegisterDto
    @Test
    public void testRegisterUser() throws Exception {
        // Mock the RegisterDto input with all fields including balance
        RegisterDto registerDto = new RegisterDto("John", "Doe", "john.doe@example.com", (byte) 1, "john_doe", "password123");

        // No response is expected from the service method, only a message
        doNothing().when(authService).register(registerDto);

        // Perform the request and check the response
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User created successfully"));
    }

    @Test
    @WithMockUser(username = "john_doe", roles = {"USER"})
    public void testLogin() throws Exception {
        // Valid LoginDto input
        LoginDto loginDto = new LoginDto("john_doe", "password123"); // Ensure the username and password meet validation requirements

        // Mock the JwtResponse that the AuthService will return
        JwtResponse jwtResponse = new JwtResponse("mock-jwt-token", "john_doe", List.of("USER"));

        // Mock the service's login method to return the JwtResponse
        when(authService.login(Mockito.any(LoginDto.class))).thenReturn(jwtResponse);

        // Perform the POST request and check the response
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)))  // Send valid JSON content
                .andExpect(status().isOk())  // Expect HTTP 200 OK status
                .andExpect(cookie().exists("token"))  // The JWT token cookie should exist
                .andExpect(cookie().value("token", "mock-jwt-token"));  // The token value should match
    }

}
