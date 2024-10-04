package lt.ca.javau10.sakila.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import lt.ca.javau10.sakila.models.Address;
import lt.ca.javau10.sakila.models.City;
import lt.ca.javau10.sakila.models.Country;
import lt.ca.javau10.sakila.models.Store;
import lt.ca.javau10.sakila.models.dto.StoreDto;
import lt.ca.javau10.sakila.repositories.StoreRepository;


public class StoreServiceTest {

	@InjectMocks
    private StoreService storeService;

    @Mock
    private StoreRepository storeRepository;

    private Store store1;
    private Store store2;
    private Address address1;
    private Address address2;
    private City city1;
    private City city2;
    private Country country1;
    private Country country2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create sample Country objects
        country1 = new Country("USA");
        country1.setCountryId((short) 1);
        country2 = new Country("Canada");
        country2.setCountryId((short) 2);

        // Create sample City objects
        city1 = new City("New York", country1);
        city1.setCityId((short) 1);
        city2 = new City("Toronto", country2);
        city2.setCityId((short) 2);

        // Create sample Address objects
        address1 = new Address("123 Main St", "Manhattan", city1);
        address2 = new Address("456 Elm St", "Downtown", city2);

        // Create sample Store objects
        store1 = new Store();
        store1.setStoreId((byte) 1);
        store1.setAddress(address1);

        store2 = new Store();
        store2.setStoreId((byte) 2);
        store2.setAddress(address2);
    }

    @Test
    public void testGetAllStores() {
        // Arrange: Mock the repository to return a list of stores
        when(storeRepository.findAll()).thenReturn(Arrays.asList(store1, store2));

        // Act: Call the service method
        List<StoreDto> storeDtos = storeService.getAllStores();

        // Assert: Verify the results
        assertNotNull(storeDtos);
        assertEquals(2, storeDtos.size());

        // Verify the details of the first store
        assertEquals(store1.getStoreId(), storeDtos.get(0).getStoreId());
        assertEquals(store1.getAddress().getCity().getCountry().getCountry(), storeDtos.get(0).getCountry());
        assertEquals(store1.getAddress().getCity().getCity(), storeDtos.get(0).getCity());
        assertEquals(store1.getAddress().getAddress(), storeDtos.get(0).getAddress());

        // Verify the details of the second store
        assertEquals(store2.getStoreId(), storeDtos.get(1).getStoreId());
        assertEquals(store2.getAddress().getCity().getCountry().getCountry(), storeDtos.get(1).getCountry());
        assertEquals(store2.getAddress().getCity().getCity(), storeDtos.get(1).getCity());
        assertEquals(store2.getAddress().getAddress(), storeDtos.get(1).getAddress());

        // Verify that the repository was called once
        verify(storeRepository, times(1)).findAll();
    }
}
