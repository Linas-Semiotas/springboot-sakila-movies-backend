package lt.ca.javau10.sakila.controllers;

import lt.ca.javau10.sakila.models.dto.RentalDto;
import lt.ca.javau10.sakila.security.utils.JwtUtil;
import lt.ca.javau10.sakila.services.CustomUserDetailsService;
import lt.ca.javau10.sakila.services.RentalService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RentalController.class)
@AutoConfigureMockMvc(addFilters = false) // Bypass security
public class RentalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RentalService rentalService;
    
    @MockBean
    private JwtUtil jwtUtil;
    
    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    private RentalDto rentalDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        rentalDto = new RentalDto((short) 1, "Movie Title", new BigDecimal("12.99"), new BigDecimal("50.00"), (short) 7);
    }

    @Test
    public void testGetAllRentals() throws Exception {
        List<RentalDto> rentals = Arrays.asList(rentalDto); // Use rentalDto initialized in setUp

        when(rentalService.getAllRentals()).thenReturn(rentals);

        mockMvc.perform(get("/api/rental"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Movie Title"))
                .andExpect(jsonPath("$[0].rentalRate").value(12.99))
                .andExpect(jsonPath("$[0].replacementCost").value(50.00))
                .andExpect(jsonPath("$[0].rentalDuration").value(7));
    }

    @Test
    public void testRentMovie() throws Exception {
        // Mock Principal object
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("User1");

        mockMvc.perform(post("/api/rental/rent")
                .principal(principal) // Add the principal here
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rentalDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Movie rented successfully"));

        verify(rentalService).rentMovie(eq(rentalDto.getId()), eq("User1")); // Pass expected username
    }

    @Test
    public void testGetRentalById() throws Exception {
        // Mock Principal object
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("User1");

        when(rentalService.getRentalById((short) 1, "User1")).thenReturn(rentalDto);

        mockMvc.perform(get("/api/rental/{id}", 1)
                .principal(principal)) // Add the principal here
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Movie Title"))
                .andExpect(jsonPath("$.rentalRate").value(12.99));
    }
}
