package lt.ca.javau10.sakila.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lt.ca.javau10.sakila.exceptions.ResourceNotFoundException;
import lt.ca.javau10.sakila.models.Customer;
import lt.ca.javau10.sakila.models.Language;
import lt.ca.javau10.sakila.models.User;
import lt.ca.javau10.sakila.models.dto.AdminUserDto;
import lt.ca.javau10.sakila.repositories.CustomerRepository;
import lt.ca.javau10.sakila.repositories.LanguageRepository;
import lt.ca.javau10.sakila.repositories.MovieRepository;
import lt.ca.javau10.sakila.repositories.UserRepository;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final LanguageRepository languageRepository;
    private final MovieRepository movieRepository;

    public AdminService(UserRepository userRepository, CustomerRepository customerRepository, LanguageRepository languageRepository, MovieRepository movieRepository) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.languageRepository = languageRepository;
        this.movieRepository = movieRepository;
    }
    
    //ADMIN -> USERS
    
    public List<AdminUserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<AdminUserDto> userDtos = new ArrayList<>();
        for (User user : users) {
            AdminUserDto dto = new AdminUserDto();
            dto.setUserId(user.getUserId());
            dto.setUsername(user.getUsername());
            dto.setUserRole(user.getRoles().contains("USER"));
            dto.setAdminRole(user.getRoles().contains("ADMIN"));
            dto.setEnabled(user.getCustomer().getActive() == 1);
            userDtos.add(dto);
        }
        return userDtos;
    }

    public void updateUser(int userId, AdminUserDto updateDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Customer customer = user.getCustomer();

        // Update enabled status
        customer.setActive(updateDto.isEnabled() ? (byte) 1 : (byte) 0);
        customerRepository.save(customer);

        // Update roles
        updateRole(user, "USER", updateDto.isUserRole());
        updateRole(user, "ADMIN", updateDto.isAdminRole());

        userRepository.save(user);
    }

    private void updateRole(User user, String roleName, boolean addRole) {
        Set<String> roles = user.getRoles();
        if (addRole) {
            roles.add(roleName);
        } else {
            roles.remove(roleName);
        }
        user.setRoles(roles);
    }
    
    //ADMIN -> MOVIES
    
    //ADMIN -> MOVIES -> LANGUAGE
    
    public List<Language> getAllLanguages() {
        return languageRepository.findAll();
    }

    public Language addLanguage(String name) {
        if (languageRepository.existsByName(name)) {
            throw new IllegalArgumentException("Language already exists");
        }
        Language language = new Language(name);
        return languageRepository.save(language);
    }

    public void deleteLanguage(Short id) {
        Language language = languageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Language not found"));

        if (movieRepository.existsByLanguage(language)) {
            throw new IllegalArgumentException("Cannot delete language because it is used by one or more movies.");
        }

        languageRepository.delete(language);
    }
}