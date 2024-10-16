package lt.ca.javau10.sakila.exceptions;

public class InventoryNotAvailableException extends RuntimeException  {
	private static final long serialVersionUID = 1L;

    public InventoryNotAvailableException(String message) {
        super(message);
    }
}
