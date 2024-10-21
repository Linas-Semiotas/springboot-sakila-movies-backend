package lt.ca.javau10.sakila.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lt.ca.javau10.sakila.models.User;
import lt.ca.javau10.sakila.exceptions.UsernameAlreadyExistsException;
import lt.ca.javau10.sakila.models.Address;
import lt.ca.javau10.sakila.models.City;
import lt.ca.javau10.sakila.models.Country;
import lt.ca.javau10.sakila.models.Customer;
import lt.ca.javau10.sakila.models.dto.LoginDto;
import lt.ca.javau10.sakila.models.dto.RegisterDto;
import lt.ca.javau10.sakila.repositories.AddressRepository;
import lt.ca.javau10.sakila.repositories.CityRepository;
import lt.ca.javau10.sakila.repositories.CountryRepository;
import lt.ca.javau10.sakila.repositories.CustomerRepository;
import lt.ca.javau10.sakila.repositories.UserRepository;
import lt.ca.javau10.sakila.security.responses.JwtResponse;
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
    
    //HELPER
    
    public Optional<User> getUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}
    
    //REGISTER
    
    @Transactional
	public void register(RegisterDto registerDto) {
		Optional<User> existingUser = userRepository.findByUsername(registerDto.getUsername());
		if (existingUser.isPresent()) {
			throw new UsernameAlreadyExistsException("Username is already taken");
	    }
		
		Country country = countryRepository.findByCountry("")
		        .orElseGet(() -> countryRepository.save(new Country("")));
		
		City city = new City("", country);
	    City savedCity = cityRepository.save(city);
		
        Address defaultAddress = new Address("", "", "", "", savedCity);
        Address savedAddress = addressRepository.save(defaultAddress);

        String encryptedPassword = passwordEncoder.encode(registerDto.getPassword());

        boolean adminExists = userRepository.existsByRolesContaining("ADMIN");

        Set<String> roles = adminExists ? Set.of("USER") : Set.of("ADMIN");
        
        User user = new User(registerDto.getUsername(), encryptedPassword, null, roles, 0.0);
        User savedUser = userRepository.save(user);

        Customer customer = new Customer(registerDto.getFirstName(), registerDto.getLastName(), registerDto.getEmail(), registerDto.getStoreId(), savedAddress, (byte) 1);
        customer.setUser(savedUser);
        customerRepository.save(customer);
    }
	
    //LOGIN
	
    public JwtResponse login(LoginDto loginDto) {
    	Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        
        String jwt = jwtUtil.generateToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new JwtResponse(jwt, userDetails.getUsername(), roles);
    }
    
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || "anonymousUser".equals(authentication.getPrincipal())) {
            return ResponseEntity.ok(Map.of("message", "No user is logged in"));
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());

        return ResponseEntity.ok(Map.of(
            "username", userDetails.getUsername(),
            "roles", roles
        ));
    }
    
    public JwtResponse refreshToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Generate a new token
        String newJwt = jwtUtil.generateToken(userDetails);

        // Create a new JwtResponse (assuming you have this model)
        List<String> roles = userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());

        return new JwtResponse(newJwt, userDetails.getUsername(), roles);
    }
}
