package lt.ca.javau10.sakila.services;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import lt.ca.javau10.sakila.models.User;
import lt.ca.javau10.sakila.repositories.UserRepository;

@Component
public class UserPasswordInitializer {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserPasswordInitializer(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void initializeUserCredentials() {
        // Fetch users whose password is an empty string
        List<User> usersNeedingCredentialUpdate = userRepository.findByPassword("");

        for (User user : usersNeedingCredentialUpdate) {
            String rawPassword = "password";

            String hashedPassword = passwordEncoder.encode(rawPassword);

            user.setPassword(hashedPassword);

            userRepository.save(user);
        }
    }

}
