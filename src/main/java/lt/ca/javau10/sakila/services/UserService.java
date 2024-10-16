package lt.ca.javau10.sakila.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lt.ca.javau10.sakila.exceptions.InsufficientBalanceException;
import lt.ca.javau10.sakila.exceptions.InvalidCurrentPasswordException;
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
    
    //HELPER
    
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
    
    //ORDERS
    
    @Transactional(readOnly = true)
    public List<OrdersDto> getOrdersForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<Rental> rentals = rentalRepository.findAllByUserId(user.getUserId());
        return rentals.stream()
                .map(rental -> new OrdersDto(
                    rental.getId(),
                    rental.getRentalDate(),
                    rental.getReturnDate(),
                    rental.getInventory().getMovie().getTitle()))
                .collect(Collectors.toList());
    }
    
    //BALANCE
    
    public Double getUserBalance(String username) {
        User user = getUserByUsername(username);
        return user.getBalance();
    }

    @Transactional
    public Double addBalance(String username, Double amount) {
    	if (amount == null || amount <= 0) { // Validation happens here
            throw new InsufficientBalanceException("Amount must be greater than 0");
        }
    	
        User user = getUserByUsername(username);
        Double newBalance = user.getBalance() + amount;
        user.setBalance(newBalance);
        userRepository.save(user);
        return newBalance;
    }
    
    //PROFILE
    
    @Transactional(readOnly = true)
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
    @Transactional
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
    @Transactional(readOnly = true)
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
    @Transactional
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

        Country oldCountry = city.getCountry();
        city.setCountry(newCountry);
        cityRepository.save(city);

        if (!cityRepository.existsByCountry(oldCountry)) {
            countryRepository.delete(oldCountry);
        }

        addressRepository.save(address);
    }
    
    //SECURITY
    
    public void changePassword(String username, String currentPassword, String newPassword) {
        User user = getUserByUsername(username);

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new InvalidCurrentPasswordException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}