package lt.ca.javau10.sakila.controllers;

import lt.ca.javau10.sakila.models.dto.StoreDto;
import lt.ca.javau10.sakila.security.utils.JwtUtil;
import lt.ca.javau10.sakila.services.CustomUserDetailsService;
import lt.ca.javau10.sakila.services.StoreService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StoresController.class)
@AutoConfigureMockMvc(addFilters = false)
public class StoresControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StoreService storeService;
    
    @MockBean
    private JwtUtil jwtUtil;
    
    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    private StoreDto storeDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        storeDto = new StoreDto((byte) 1, "123 Main St", "City One", "Country One");
    }

    @Test
    public void testGetAllStores() throws Exception {
        List<StoreDto> stores = Arrays.asList(storeDto);

        when(storeService.getAllStores()).thenReturn(stores);

        mockMvc.perform(get("/api/stores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].storeId").value(1))
                .andExpect(jsonPath("$[0].address").value("123 Main St"))
		        .andExpect(jsonPath("$[0].city").value("City One"))
		        .andExpect(jsonPath("$[0].country").value("Country One"));
    }
}
