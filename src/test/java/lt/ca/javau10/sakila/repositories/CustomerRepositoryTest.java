package lt.ca.javau10.sakila.repositories;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.Set;

import lt.ca.javau10.sakila.models.Address;
import lt.ca.javau10.sakila.models.City;
import lt.ca.javau10.sakila.models.Country;
import lt.ca.javau10.sakila.models.Customer;
import lt.ca.javau10.sakila.models.User;

@DataJpaTest  // Configures an in-memory database and loads repository beans
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AddressRepository addressRepository;
    
    @Autowired
    private CountryRepository countryRepository;  // Add the necessary repositories
    
    @Autowired
    private CityRepository cityRepository;

    @Test
    public void testFindByUser_UserId() {
        // Given: Create and save a new user
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setBalance(100.0);  // Ensure all fields are set
        user.setRoles(Set.of("ROLE_USER"));
        user = userRepository.save(user);  // Save user and get generated userId

        // Given: Create and save a new country
        Country country = new Country();
        country.setCountry("USA");
        country = countryRepository.save(country);  // Save the country

        // Given: Create and save a new city associated with the country
        City city = new City();
        city.setCity("New York");
        city.setCountry(country);
        city = cityRepository.save(city);  // Save the city

        // Given: Create and save a new address associated with the city
        Address address = new Address();
        address.setAddress("123 Main Street");
        address.setCity(city);  // Associate the city with the address
        address.setPostalCode("10001");
        address.setDistrict("Manhattan");
        address.setPhone("1234567890");
        address = addressRepository.save(address);  // Save the address

        // Create and save the customer with a valid storeId and associated address
        Customer customer = new Customer();
        customer.setUser(user);  // Associate the user with the customer
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("john.doe@example.com");
        customer.setStoreId((byte) 1);  // Set a valid storeId
        customer.setAddress(address);  // Associate the saved address with the customer
        customer.setActive((byte) 1);  // Set active status

        // Save the customer
        customerRepository.save(customer);

        // When: Retrieve the customer by userId
        Optional<Customer> foundCustomer = customerRepository.findByUser_UserId(user.getUserId());

        // Then: Verify the customer was found and the details are correct
        assertTrue(foundCustomer.isPresent(), "Customer should be found");
        assertEquals("John", foundCustomer.get().getFirstName(), "First name should be 'John'");
        assertEquals("Doe", foundCustomer.get().getLastName(), "Last name should be 'Doe'");
        assertEquals("testuser", foundCustomer.get().getUser().getUsername(), "Username should be 'testuser'");
        assertEquals("New York", foundCustomer.get().getAddress().getCity().getCity(), "City should be 'New York'");
        assertEquals("USA", foundCustomer.get().getAddress().getCity().getCountry().getCountry(), "Country should be 'USA'");
    }

    @Test
    public void testFindByUser_UserId_NotFound() {
        // When: Try to find a customer by a non-existent userId
        Optional<Customer> foundCustomer = customerRepository.findByUser_UserId(99);

        // Then: Verify the customer is not found
        assertFalse(foundCustomer.isPresent(), "Customer should not be found for non-existent userId");
    }
}