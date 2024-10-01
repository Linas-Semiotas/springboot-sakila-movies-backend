package lt.ca.javau10.sakila.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lt.ca.javau10.sakila.dto.RegisterDto;
import lt.ca.javau10.sakila.entities.Address;
import lt.ca.javau10.sakila.entities.City;
import lt.ca.javau10.sakila.entities.Customer;
import lt.ca.javau10.sakila.entities.User;
import lt.ca.javau10.sakila.repositories.AddressRepository;
import lt.ca.javau10.sakila.repositories.CustomerRepository;
import lt.ca.javau10.sakila.repositories.UserRepository;

@Service
public class RegisterService {

    @Autowired
    private AddressRepository addressRepository;
	
	@Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;
	
	@Autowired
    private BCryptPasswordEncoder passwordEncoder;
	
	public String register(RegisterDto registerDto) {
		User existingUser = userRepository.findByUsername(registerDto.getUsername());
        if (existingUser != null) {
            return "Username already exists";
        }
        
        City defaultCity = new City();
        defaultCity.setCityId((short) 1);
		
        Address defaultAddress = new Address("Default Address", "Default District", defaultCity);
        Address savedAddress = addressRepository.save(defaultAddress);

        String encryptedPassword = passwordEncoder.encode(registerDto.getPassword());
        User user = new User(registerDto.getUsername(), encryptedPassword, null);
        User savedUser = userRepository.save(user);

        Customer customer = new Customer(registerDto.getFirstName(), registerDto.getLastName(), registerDto.getEmail(), registerDto.getStoreId(), savedAddress);
        customer.setUser(savedUser);
        customerRepository.save(customer);
        
        return "Registration successful";
    }
}
