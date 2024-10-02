package lt.ca.javau10.sakila.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lt.ca.javau10.sakila.dto.RegisterDto;
import lt.ca.javau10.sakila.entities.Address;
import lt.ca.javau10.sakila.entities.City;
import lt.ca.javau10.sakila.entities.Customer;
import lt.ca.javau10.sakila.entities.User;
import lt.ca.javau10.sakila.repositories.AddressRepository;
import lt.ca.javau10.sakila.repositories.CustomerRepository;
import lt.ca.javau10.sakila.repositories.UserRepository;

public class AuthServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister_UsernameAlreadyExists() {
        // Arrange
    	RegisterDto registerDto = new RegisterDto("First", "Last", "email@example.com", (byte) 1, "existingUser", "password");

        User existingUser = new User("existingUser", "password", null);
        
        when(userRepository.findByUsername("existingUser")).thenReturn(existingUser);

        // Act
        String result = authService.register(registerDto);

        // Assert
        assertEquals("Username already exists", result);
        verify(userRepository, times(1)).findByUsername("existingUser");
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void testRegister_SuccessfulRegistration() {
        // Arrange
    	RegisterDto registerDto = new RegisterDto("First", "Last", "email@example.com", (byte) 1, "newUser", "password");
        when(userRepository.findByUsername("newUser")).thenReturn(null); // Username does not exist
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        City city = new City();
        city.setCityId((short) 1);

        Address defaultAddress = new Address("Default Address", "Default District", city);
        when(addressRepository.save(any(Address.class))).thenReturn(defaultAddress);

        User newUser = new User("newUser", "encodedPassword", null);
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        Customer savedCustomer = new Customer("First", "Last", "email@example.com", (byte) 1, defaultAddress);
        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        // Act
        String result = authService.register(registerDto);

        // Assert
        assertEquals("Registration successful", result);
        verify(userRepository, times(1)).findByUsername("newUser");
        verify(userRepository, times(1)).save(any(User.class));
        verify(customerRepository, times(1)).save(any(Customer.class));
        verify(addressRepository, times(1)).save(any(Address.class));
    }
}