package lt.ca.javau10.sakila.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ValidationExceptionHandler {
	
//	@ExceptionHandler(MethodArgumentNotValidException.class)
//	@ResponseStatus(HttpStatus.BAD_REQUEST)
//	public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
//	    Map<String, Object> body = new HashMap<>();
//	    body.put("message", "Validation failed");
//
//	    Map<String, String> fieldErrors = new HashMap<>();
//	    ex.getBindingResult().getFieldErrors().forEach(error -> {
//	        String fieldName = error.getField();
//	        String errorMessage = error.getDefaultMessage();
//	        fieldErrors.put(fieldName, errorMessage);
//	    });
//
//	    body.put("errors", fieldErrors);
//	    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
//	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
	    Map<String, String> errors = new HashMap<>();
	    ex.getBindingResult().getFieldErrors().forEach(error -> 
	        errors.put(error.getField(), error.getDefaultMessage()));
	    
	    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(UsernameAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", "Validation failed: Username already exists");
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidCurrentPasswordException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Map<String, Object>> handleInvalidCurrentPasswordException(InvalidCurrentPasswordException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", "Invalid current password");
        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }
	
}