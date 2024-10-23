package lt.ca.javau10.sakila.utils;

import java.security.SecureRandom;
import java.util.Base64;

import org.slf4j.Logger;

public class Utils {
	
	private static final String RESET = "\033[0m";
    private static final String BRIGHT_BLUE = "\033[94m";
    private static final String BRIGHT_GREEN = "\033[92m";
    private static final String BRIGHT_MAGENTA = "\033[95m";
    private static final String BRIGHT_RED = "\033[91m";

	
	//Takes string and turn first letter of every word to capital letter
	public static String capitalize(String str) {
		
	    if (str == null || str.isEmpty()) {
	        return str;
	    }
	    
	    String[] words = str.split("\\s+");
	    
	    StringBuilder capitalizedWords = new StringBuilder();
	    for (String word : words) {
	        if (!word.isEmpty()) {
	            capitalizedWords.append(word.substring(0, 1).toUpperCase())
	                            .append(word.substring(1).toLowerCase())
	                            .append(" ");
	        }
	    }
	    return capitalizedWords.toString().trim();
	}
	
	// Utility method for [AUTH] log with BRIGHT_BLUE color
    public static void infoAuth(Logger logger, String message, Object... args) {
        logger.info(BRIGHT_BLUE + "[AUTH] " + RESET + message, args);
    }

    // Utility method for [USER] log with BRIGHT_GREEN color
    public static void infoUser(Logger logger, String message, Object... args) {
        logger.info(BRIGHT_GREEN + "[USER] " + RESET + message, args);
    }

    // Utility method for [ADMIN] log with BRIGHT_MAGENTA color
    public static void infoAdmin(Logger logger, String message, Object... args) {
        logger.info(BRIGHT_MAGENTA + "[ADMIN] " + RESET + message, args);
    }
    
    // Utility method for [ERROR] log with BRIGHT_RED color
    public static void errorAny(Logger logger, String message, Object... args) {
        logger.info(BRIGHT_RED + "[ERROR] " + RESET + message, args);
    }
    
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[16]; // 16 bytes for salt
        random.nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes); // Convert to Base64 string
    }
}
