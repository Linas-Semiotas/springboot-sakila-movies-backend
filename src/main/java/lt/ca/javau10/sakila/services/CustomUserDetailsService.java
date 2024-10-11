package lt.ca.javau10.sakila.services;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthService authService;

    public CustomUserDetailsService(@Lazy AuthService authService) {
        this.authService = authService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return authService.getUserByUsername(username)
                .map(user -> User.withUsername(user.getUsername())
                                 .password(user.getPassword())
                                 .roles(user.getRoles().toArray(new String[0]))  // Convert roles to String array
                                 .build())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
