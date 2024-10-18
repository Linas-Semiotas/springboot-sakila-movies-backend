package lt.ca.javau10.sakila.utils;

public class Utils {
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
}
