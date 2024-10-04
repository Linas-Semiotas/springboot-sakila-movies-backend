package lt.ca.javau10.sakila.services;

import java.util.Optional;
import java.util.Set;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lt.ca.javau10.sakila.models.User;
import lt.ca.javau10.sakila.models.Address;
import lt.ca.javau10.sakila.models.City;
import lt.ca.javau10.sakila.models.Customer;
import lt.ca.javau10.sakila.models.dto.RegisterDto;
import lt.ca.javau10.sakila.repositories.AddressRepository;
import lt.ca.javau10.sakila.repositories.CustomerRepository;
import lt.ca.javau10.sakila.repositories.UserRepository;
import lt.ca.javau10.sakila.security.utils.JwtUtil;

@Service
public class AuthService {

	private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthService(AddressRepository addressRepository, 
                       CustomerRepository customerRepository, 
                       UserRepository userRepository, 
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil, 
                       AuthenticationManager authenticationManager) {
        this.addressRepository = addressRepository;
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }
    
	public String register(RegisterDto registerDto) {
		Optional<User> existingUser = userRepository.findByUsername(registerDto.getUsername());
		if (existingUser.isPresent()) {
	        return "Username already exists";
	    }
        
        City defaultCity = new City();
        defaultCity.setCityId((short) 1);
		
        Address defaultAddress = new Address("Default Address", "Default District", defaultCity);
        Address savedAddress = addressRepository.save(defaultAddress);

        String encryptedPassword = passwordEncoder.encode(registerDto.getPassword());
        User user = new User(registerDto.getUsername(), encryptedPassword, null, Set.of("USER"));
        User savedUser = userRepository.save(user);

        Customer customer = new Customer(registerDto.getFirstName(), registerDto.getLastName(), registerDto.getEmail(), registerDto.getStoreId(), savedAddress);
        customer.setUser(savedUser);
        customerRepository.save(customer);
        
        return "Registration successful";
    }

    public Optional<User> getUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	
    public String login(String username, String password) {
        try {
        	Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return jwtUtil.generateToken(userDetails);
        } catch (BadCredentialsException e) {
            return "Invalid username or password";
        }
    }
}
