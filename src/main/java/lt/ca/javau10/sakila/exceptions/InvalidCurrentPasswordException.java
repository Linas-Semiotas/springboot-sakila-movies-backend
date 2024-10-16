package lt.ca.javau10.sakila.exceptions;

public class InvalidCurrentPasswordException extends RuntimeException  {
	private static final long serialVersionUID = 1L;

	public InvalidCurrentPasswordException(String message) {
        super(message);
    }
}
