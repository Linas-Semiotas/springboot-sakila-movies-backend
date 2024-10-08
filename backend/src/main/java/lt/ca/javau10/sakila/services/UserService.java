package lt.ca.javau10.sakila.services;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lt.ca.javau10.sakila.exceptions.ResourceNotFoundException;
import lt.ca.javau10.sakila.models.Address;
import lt.ca.javau10.sakila.models.Customer;
import lt.ca.javau10.sakila.models.User;
import lt.ca.javau10.sakila.models.dto.PersonalInfoDto;
import lt.ca.javau10.sakila.repositories.AddressRepository;
import lt.ca.javau10.sakila.repositories.CustomerRepository;
import lt.ca.javau10.sakila.repositories.UserRepository;

@Service
public class UserService {

    private UserRepository userRepository;
    private CustomerRepository  customerRepository;
    private AddressRepository  addressRepository;
    private final PasswordEncoder passwordEncoder;
    
    public UserService(UserRepository userRepository, CustomerRepository  customerRepository, AddressRepository  addressRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.customerRepository = customerRepository;
		this.addressRepository = addressRepository;
		this.passwordEncoder = passwordEncoder;
	}
    
    
    public Double getUserBalance(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user.getBalance();
    }

    @Transactional
    public Double addBalance(String username, Double amount) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Double newBalance = user.getBalance() + amount;
        user.setBalance(newBalance);
        userRepository.save(user);
        return newBalance;
    }
    
  //Change password
    public void changePassword(String username, String currentPassword, String newPassword) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        // Verify the current password
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new BadCredentialsException("Current password is incorrect");
        }
        // Update the user's password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
    
    public int getUserIdByUsername(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user.getUserId();
    }
    
    //Get personal information based on user ID
    public PersonalInfoDto getPersonalInfo(int userId) {
        Customer customer = customerRepository.findByUser_UserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        Address address = addressRepository.findById(customer.getAddress().getAddressId())
            .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        PersonalInfoDto dto = new PersonalInfoDto();
        dto.setFirstName(customer.getFirstName());
        dto.setLastName(customer.getLastName());
        dto.setEmail(customer.getEmail());
        dto.setPhone(address.getPhone());

        return dto;
    }

    //Update personal information
    public void updatePersonalInfo(int userId, PersonalInfoDto personalInfoDto) {
        Customer customer = customerRepository.findByUser_UserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        Address address = addressRepository.findById(customer.getAddress().getAddressId())
            .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        customer.setFirstName(personalInfoDto.getFirstName());
        customer.setLastName(personalInfoDto.getLastName());
        customer.setEmail(personalInfoDto.getEmail());
        address.setPhone(personalInfoDto.getPhone());

        customerRepository.save(customer);
        addressRepository.save(address);
    }
}