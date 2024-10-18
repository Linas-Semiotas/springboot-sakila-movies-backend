package lt.ca.javau10.sakila.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import lt.ca.javau10.sakila.exceptions.InsufficientBalanceException;
import lt.ca.javau10.sakila.exceptions.InvalidCurrentPasswordException;
import lt.ca.javau10.sakila.models.*;
import lt.ca.javau10.sakila.models.dto.OrdersDto;
import lt.ca.javau10.sakila.models.dto.PersonalInfoDto;
import lt.ca.javau10.sakila.repositories.*;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetOrdersForUser() {
        // Given a user
        User user = new User();
        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.of(user));

        // Given a rental with a movie
        Movie movie = new Movie();
        movie.setFilmId((short) 1);
        movie.setTitle("Inception");
        Inventory inventory = new Inventory();
        inventory.setMovie(movie);

        Rental rental = new Rental();
        rental.setInventory(inventory);
        rental.setRentalDate(LocalDateTime.now());
        rental.setReturnDate(LocalDateTime.now().plusDays(7));

        // Mocking rental repository behavior
        when(rentalRepository.findAllByUserId(user.getUserId())).thenReturn(List.of(rental));

        // When fetching orders
        List<OrdersDto> orders = userService.getOrdersForUser("john_doe");

        // Then assert results
        assertNotNull(orders);
        assertEquals(1, orders.size());
        assertEquals("Inception", orders.get(0).getTitle());
    }

    @Test
    public void testGetUserBalance() {
        User user = new User();
        user.setBalance(100.0);
        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.of(user));

        Double balance = userService.getUserBalance("john_doe");

        assertNotNull(balance);
        assertEquals(100.0, balance);
    }

    @Test
    public void testAddBalance_Success() {
        User user = new User();
        user.setBalance(100.0);
        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.of(user));

        Double newBalance = userService.addBalance("john_doe", 50.0);

        assertNotNull(newBalance);
        assertEquals(150.0, newBalance);
        verify(userRepository).save(user);
    }

    @Test
    public void testAddBalance_InvalidAmount() {
        assertThrows(InsufficientBalanceException.class, () -> userService.addBalance("john_doe", -50.0));
        verify(userRepository, never()).save(any());
    }

    @Test
    public void testGetPersonalInfo() {
        // Create and set up Customer with Address
        Address address = new Address();
        address.setPhone("123456789");
        address.setAddressId((short) 1); // Ensure address has an ID if necessary

        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("john.doe@example.com");
        customer.setAddress(address); // Set the address for the customer

        when(customerRepository.findByUser_UserId(1)).thenReturn(Optional.of(customer));
        when(addressRepository.findById(anyShort())).thenReturn(Optional.of(address));

        // Call the method under test
        PersonalInfoDto info = userService.getPersonalInfo(1);

        // Assert results
        assertNotNull(info);
        assertEquals("John", info.getFirstName());
        assertEquals("Doe", info.getLastName());
        assertEquals("123456789", info.getPhone());
    }

    @Test
    public void testUpdatePersonalInfo() {
        // Create the Address and Customer objects
        Address address = new Address();
        address.setPhone("987654321");
        address.setAddressId((short) 1); // Ensure address has an ID if necessary

        Customer customer = new Customer();
        customer.setAddress(address); // Set the address for the customer

        // PersonalInfoDto to update
        PersonalInfoDto dto = new PersonalInfoDto();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("john.doe@example.com");
        dto.setPhone("987654321");

        when(customerRepository.findByUser_UserId(1)).thenReturn(Optional.of(customer));
        when(addressRepository.findById(anyShort())).thenReturn(Optional.of(address));

        // Call the method under test
        userService.updatePersonalInfo(1, dto);

        // Assert updated values
        assertEquals("John", customer.getFirstName());
        assertEquals("Doe", customer.getLastName());
        assertEquals("987654321", address.getPhone());

        // Verify that the repositories were called to save the updated entities
        verify(customerRepository).save(customer);
        verify(addressRepository).save(address);
    }


    @Test
    public void testChangePassword_Success() {
        User user = new User();
        user.setPassword("encoded_old_password");
        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("old_password", "encoded_old_password")).thenReturn(true);
        when(passwordEncoder.encode("new_password")).thenReturn("encoded_new_password");

        userService.changePassword("john_doe", "old_password", "new_password");

        assertEquals("encoded_new_password", user.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    public void testChangePassword_InvalidCurrentPassword() {
        User user = new User();
        user.setPassword("encoded_old_password");
        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong_password", "encoded_old_password")).thenReturn(false);

        assertThrows(InvalidCurrentPasswordException.class, () -> userService.changePassword("john_doe", "wrong_password", "new_password"));

        verify(userRepository, never()).save(any());
    }
}
