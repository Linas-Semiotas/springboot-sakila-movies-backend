package lt.ca.javau10.sakila.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.Set;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import lt.ca.javau10.sakila.models.*;
import lt.ca.javau10.sakila.models.dto.LoginDto;
import lt.ca.javau10.sakila.models.dto.RegisterDto;
import lt.ca.javau10.sakila.repositories.*;
import lt.ca.javau10.sakila.security.responses.JwtResponse;
import lt.ca.javau10.sakila.security.utils.JwtUtil;
import lt.ca.javau10.sakila.exceptions.UsernameAlreadyExistsException;

public class AuthServiceTest {

	@InjectMocks
    private AuthService authService;

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
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test for successful registration
    @Test
    public void testRegister_Success() {
        RegisterDto registerDto = new RegisterDto("John", "Doe", "john@example.com", (byte) 1, "john_doe", "password");

        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encoded_password");
        Country country = new Country("USA");
        when(countryRepository.findByCountry("")).thenReturn(Optional.of(country));
        City city = new City("New York", country);
        when(cityRepository.save(any(City.class))).thenReturn(city);
        Address address = new Address("", "", "", "", city);
        when(addressRepository.save(any(Address.class))).thenReturn(address);
        User user = new User("john_doe", "encoded_password", null, Set.of("USER"), 100.0);
        when(userRepository.save(any(User.class))).thenReturn(user);

        authService.register(registerDto);

        verify(userRepository, times(1)).save(any(User.class));
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    // Test if UsernameAlreadyExistsException is thrown when the username is already taken
    @Test
    public void testRegister_UsernameAlreadyExists() {
        RegisterDto registerDto = new RegisterDto("John", "Doe", "john@example.com", (byte) 1, "john_doe", "password");

        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.of(new User()));

        assertThrows(UsernameAlreadyExistsException.class, () -> authService.register(registerDto));

        verify(userRepository, never()).save(any(User.class));
    }

    // Test for successful login
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void testLogin_Success() {
        // Arrange
        LoginDto loginDto = new LoginDto("john_doe", "password");

        // Mock user repository to return a user
        User mockUser = new User("john_doe", "encoded_password", null, Set.of("USER"), 100.0);
        when(userRepository.findByUsername("john_doe")).thenReturn(Optional.of(mockUser));

        // Mock the authentication process
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        // Mock the user details and authorities
        when(userDetails.getUsername()).thenReturn("john_doe");

        // Mock the GrantedAuthority
        GrantedAuthority authority = mock(GrantedAuthority.class);
        when(authority.getAuthority()).thenReturn("ROLE_USER");

        // Create a properly parameterized Collection of GrantedAuthority
        Collection<GrantedAuthority> authorities = List.of(authority);
        when(userDetails.getAuthorities()).thenReturn((Collection) authorities);

        // Mock the JWT token generation
        when(jwtUtil.generateToken(userDetails)).thenReturn("mock-jwt-token");

        // Act
        JwtResponse jwtResponse = authService.login(loginDto);

        // Assert
        assertNotNull(jwtResponse);
        assertEquals("mock-jwt-token", jwtResponse.getToken());
        assertEquals("john_doe", jwtResponse.getUsername());
        assertEquals("ROLE_USER", jwtResponse.getRoles().get(0));
    }

    // Test for failed login
    @Test
    public void testLogin_Failure() {
        LoginDto loginDto = new LoginDto("invalid_user", "wrong_password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenThrow(new RuntimeException("Invalid credentials"));

        assertThrows(RuntimeException.class, () -> authService.login(loginDto));

        verify(jwtUtil, never()).generateToken(any(UserDetails.class));
    }
}
