package lt.ca.javau10.sakila.security.responses;

//Class for sending message to client side
public class MessageResponse {
    private String message;

    public MessageResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
    	this.message = message;
    }
}