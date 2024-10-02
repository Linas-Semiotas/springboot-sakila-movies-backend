package lt.ca.javau10.sakila.controllers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import lt.ca.javau10.sakila.dto.RegisterDto;
import lt.ca.javau10.sakila.services.AuthService;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @InjectMocks
    private AuthController authController;
    
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }
    
    @Test
    public void registerUser_ShouldReturnSuccessMessage() throws Exception {
    	RegisterDto registerDto = new RegisterDto("John", "Doe", "email@example.com", (byte) 1, "username", "password");

        // Perform the POST request
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));

        // Verify that the service's register method was called
        verify(authService, times(1)).register(registerDto);
    }

    @Test
    public void registerUser_ShouldReturnErrorMessage_OnException() throws Exception {
        RegisterDto registerDto = new RegisterDto("John", "Doe", "email@example.com", (byte) 1, "username", "password");

        // Simulate the service throwing an exception
        doThrow(new RuntimeException("Registration error")).when(authService).register(registerDto);

        // Perform the POST request
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isInternalServerError())  // Expect HTTP 500
                .andExpect(content().string("Error registering user"));

        // Verify that the service's register method was called
        verify(authService, times(1)).register(registerDto);
    }
}