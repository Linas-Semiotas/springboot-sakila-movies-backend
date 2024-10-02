package lt.ca.javau10.sakila.controllers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import lt.ca.javau10.sakila.dto.StoreDto;
import lt.ca.javau10.sakila.services.StoreService;

@WebMvcTest(StoresController.class)
public class StoresControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private StoreService storeService;

    @InjectMocks
    private StoresController storesController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(storesController).build();

        StoreDto store1 = new StoreDto((byte) 1, "Country One", "City One", "123 Main St");
        StoreDto store2 = new StoreDto((byte) 2, "Country Two", "City Two", "456 Elm St");
        List<StoreDto> stores = Arrays.asList(store1, store2);

        when(storeService.getAllStores()).thenReturn(stores);
    }

    @Test
    public void getAllStores_ShouldReturnListOfStores() throws Exception {
        mockMvc.perform(get("/stores")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(storeService, times(1)).getAllStores();
    }
}