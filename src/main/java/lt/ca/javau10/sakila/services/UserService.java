package lt.ca.javau10.sakila.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lt.ca.javau10.sakila.exceptions.ResourceNotFoundException;
import lt.ca.javau10.sakila.models.Address;
import lt.ca.javau10.sakila.models.City;
import lt.ca.javau10.sakila.models.Country;
import lt.ca.javau10.sakila.models.Customer;
import lt.ca.javau10.sakila.models.Rental;
import lt.ca.javau10.sakila.models.User;
import lt.ca.javau10.sakila.models.dto.AddressInfoDto;
import lt.ca.javau10.sakila.models.dto.OrdersDto;
import lt.ca.javau10.sakila.models.dto.PersonalInfoDto;
import lt.ca.javau10.sakila.repositories.AddressRepository;
import lt.ca.javau10.sakila.repositories.CityRepository;
import lt.ca.javau10.sakila.repositories.CountryRepository;
import lt.ca.javau10.sakila.repositories.CustomerRepository;
import lt.ca.javau10.sakila.repositories.RentalRepository;
import lt.ca.javau10.sakila.repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CustomerRepository  customerRepository;
    private final AddressRepository  addressRepository;
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final RentalRepository rentalRepository;
    private final PasswordEncoder passwordEncoder;
    
    public UserService(UserRepository userRepository,
    		CustomerRepository  customerRepository,
    		AddressRepository  addressRepository,
    		CityRepository cityRepository,
    		CountryRepository countryRepository,
    		RentalRepository rentalRepository,
    		PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.customerRepository = customerRepository;
		this.addressRepository = addressRepository;
		this.cityRepository = cityRepository;
		this.countryRepository = countryRepository;
		this.rentalRepository = rentalRepository;
		this.passwordEncoder = passwordEncoder;
	}
    
    //ORDERS
    
    @Transactional
    public List<OrdersDto> getOrdersForUser(Integer userId) {
        List<Rental> rentals = rentalRepository.findAllByUserId(userId);
        return rentals.stream()
                .map(this::convertToOrdersDto)
                .collect(Collectors.toList());
    }

    private OrdersDto convertToOrdersDto(Rental rental) {
        return new OrdersDto(
            rental.getId(),
            rental.getRentalDate(),
            rental.getReturnDate(),
            rental.getInventory().getMovie().getTitle()
        );
    }
    
    //BALANCE
    
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
    
    //PROFILE
    
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
    
    //Get address information based on user ID
    public AddressInfoDto getAddressInfo(int userId) {
        Customer customer = customerRepository.findByUser_UserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Address address = addressRepository.findById(customer.getAddress().getAddressId())
            .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        return new AddressInfoDto(
            address.getAddress(),
            address.getDistrict(),
            address.getPostalCode(),
            address.getCity().getCity(),
            address.getCity().getCountry().getCountry()
        );
    }

    //Update address information
    public void updateAddressInfo(int userId, AddressInfoDto addressInfoDto) {
        Customer customer = customerRepository.findByUser_UserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Address address = addressRepository.findById(customer.getAddress().getAddressId())
            .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        address.setAddress(addressInfoDto.getAddress());
        address.setDistrict(addressInfoDto.getDistrict());
        address.setPostalCode(addressInfoDto.getPostalCode());
        
        City city = address.getCity();
        
        city.setCity(addressInfoDto.getCity());
        
        Country newCountry = countryRepository.findByCountry(addressInfoDto.getCountry())
                .orElseGet(() -> countryRepository.save(new Country(addressInfoDto.getCountry())));

            // Get the old country before updating the city
            Country oldCountry = city.getCountry();

            // Update the city to associate it with the new country
            city.setCountry(newCountry);
            cityRepository.save(city);

            // Check if the old country is no longer used by any city
            if (!cityRepository.existsByCountry(oldCountry)) {
                countryRepository.delete(oldCountry);
            }

            addressRepository.save(address);
    }
    
    //SECURITY
    
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
}