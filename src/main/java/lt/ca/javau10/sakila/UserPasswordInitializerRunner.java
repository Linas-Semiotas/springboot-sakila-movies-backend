package lt.ca.javau10.sakila;
//
import org.springframework.stereotype.Component;

import lt.ca.javau10.sakila.services.UserPasswordInitializer;

import org.springframework.boot.CommandLineRunner;

@Component
public class UserPasswordInitializerRunner implements CommandLineRunner {

	private final UserPasswordInitializer userPasswordInitializer;

    public UserPasswordInitializerRunner(UserPasswordInitializer userPasswordInitializer) {
        this.userPasswordInitializer = userPasswordInitializer;
    }

    @Override
    public void run(String... args) throws Exception {
    	userPasswordInitializer.initializeUserCredentials();
    }
}
