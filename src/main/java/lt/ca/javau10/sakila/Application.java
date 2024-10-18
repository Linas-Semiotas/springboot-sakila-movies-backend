package lt.ca.javau10.sakila;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application{
	
	private static final Logger logger = LoggerFactory.getLogger(Application.class);
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		//Logger that separates startup logs from logs displayed by actions
		logger.info("========== Application Startup Complete. User Logs Begins Here ==========");
	}
}
