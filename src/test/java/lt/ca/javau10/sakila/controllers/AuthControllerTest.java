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
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
        RegisterDto registerDto = new RegisterDto("John", "Doe", "john.doe@example.com", (byte) 1, "john_doe", "password123", 100.0);

        // No response is expected from the service method, only a message
        doNothing().when(authService).register(registerDto);

        // Perform the request and check the response
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User created successfully"));
    }

    // Test for login and JWT response
    @Test
    public void testLogin() throws Exception {
    	LoginDto loginDto = new LoginDto("john_doe", "password123");
    	JwtResponse jwtResponse = new JwtResponse("mock-jwt-token", "john_doe", List.of("USER"));

    	when(authService.login(Mockito.eq(loginDto))).thenReturn(jwtResponse);
        
        // Perform the request and check the response
    	mockMvc.perform(post("/api/auth/login")
    	        .contentType(MediaType.APPLICATION_JSON)
    	        .content(objectMapper.writeValueAsString(loginDto)))
    	        .andExpect(status().isOk())
    	        .andReturn().getResponse().getContentAsString();
    }
}
