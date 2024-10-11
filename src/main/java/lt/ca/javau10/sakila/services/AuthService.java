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
import lt.ca.javau10.sakila.models.Country;
import lt.ca.javau10.sakila.models.Customer;
import lt.ca.javau10.sakila.models.dto.RegisterDto;
import lt.ca.javau10.sakila.repositories.AddressRepository;
import lt.ca.javau10.sakila.repositories.CityRepository;
import lt.ca.javau10.sakila.repositories.CountryRepository;
import lt.ca.javau10.sakila.repositories.CustomerRepository;
import lt.ca.javau10.sakila.repositories.UserRepository;
import lt.ca.javau10.sakila.security.utils.JwtUtil;

@Service
public class AuthService {

	private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthService(AddressRepository addressRepository, 
                       CustomerRepository customerRepository, 
                       UserRepository userRepository, 
                       CityRepository cityRepository,
                       CountryRepository countryRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil, 
                       AuthenticationManager authenticationManager) {
        this.addressRepository = addressRepository;
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }
    
    //Registration functionality
	public String register(RegisterDto registerDto) {
		Optional<User> existingUser = userRepository.findByUsername(registerDto.getUsername());
		if (existingUser.isPresent()) {
	        return "Username already exists";
	    }
		
		Country country = countryRepository.findByCountry("")
		        .orElseGet(() -> countryRepository.save(new Country("")));
		
		City city = new City("", country);
	    City savedCity = cityRepository.save(city);
		
        Address defaultAddress = new Address("", "", "", "", savedCity);
        Address savedAddress = addressRepository.save(defaultAddress);

        String encryptedPassword = passwordEncoder.encode(registerDto.getPassword());
        User user = new User(registerDto.getUsername(), encryptedPassword, null, Set.of("USER"), 0.0);
        User savedUser = userRepository.save(user);

        Customer customer = new Customer(registerDto.getFirstName(), registerDto.getLastName(), registerDto.getEmail(), registerDto.getStoreId(), savedAddress, (byte) 1);
        customer.setUser(savedUser);
        customerRepository.save(customer);
        
        return "Registration successful";
    }

	//Finding user in repository
    public Optional<User> getUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	
    //Login functionality
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
