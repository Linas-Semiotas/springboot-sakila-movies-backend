package lt.ca.javau10.sakila.controllers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import lt.ca.javau10.sakila.dto.RentalDto;
import lt.ca.javau10.sakila.services.RentalService;

@WebMvcTest(RentalController.class)
public class RentalControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private RentalService rentalService;

    @InjectMocks
    private RentalController rentalController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(rentalController).build();

        RentalDto rental1 = new RentalDto((short) 1, "Country One", new BigDecimal("12.99"), new BigDecimal("50.00"), (short) 7);
        RentalDto rental2 = new RentalDto((short) 2, "Country Two", new BigDecimal("15.99"), new BigDecimal("60.00"), (short) 5);
        List<RentalDto> rentals = Arrays.asList(rental1, rental2);

        when(rentalService.getAllRentals()).thenReturn(rentals);
    }

    @Test
    public void getAllRentals_ShouldReturnListOfRentals() throws Exception {
        mockMvc.perform(get("/api/rental")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(rentalService, times(1)).getAllRentals();
    }
}