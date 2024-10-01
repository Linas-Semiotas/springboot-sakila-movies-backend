package lt.ca.javau10.sakila.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpServletResponse;
import lt.ca.javau10.sakila.services.MyUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private MyUserDetailsService myUserDetailsService;
	
	@Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable())
	        .authorizeHttpRequests(authorize -> authorize
	        	.requestMatchers("/login").permitAll()
	            .anyRequest().permitAll()
	        )
	        .formLogin(form -> form
                .loginPage("/login")
                .permitAll()
                .successHandler((request, response, authentication) -> {
                    response.setStatus(HttpServletResponse.SC_OK); // Set appropriate status
                    response.getWriter().write("Login successful");
            })
            .failureHandler((request, response, authentication) -> {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Set unauthorized status
                response.getWriter().write("Login failed");
            })
            )
			.userDetailsService(myUserDetailsService);
		
		return http.build();
    }
	
    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}