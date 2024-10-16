package lt.ca.javau10.sakila.exceptions;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<Object> handleInsufficientBalanceException(InsufficientBalanceException ex, HttpServletRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", ex.getMessage());
        body.put("path", request.getRequestURI());
        body.put("status", HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InventoryNotAvailableException.class)
    public ResponseEntity<Object> handleInventoryNotAvailableException(InventoryNotAvailableException ex, HttpServletRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", ex.getMessage());
        body.put("path", request.getRequestURI());
        body.put("status", HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex, HttpServletRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", ex.getMessage());
        body.put("path", request.getRequestURI());
        body.put("status", HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MovieNotFoundException.class)
    public ResponseEntity<Object> handleMovieNotFoundException(MovieNotFoundException ex, HttpServletRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", ex.getMessage());
        body.put("path", request.getRequestURI());
        body.put("status", HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }
	
	@ExceptionHandler(UsernameAlreadyExistsException.class)
	public ResponseEntity<Object> handleUsernameAlreadyExists(UsernameAlreadyExistsException ex, HttpServletRequest request) {
	    Map<String, Object> body = new HashMap<>();
	    body.put("message", ex.getMessage());
	    body.put("path", request.getRequestURI());
	    body.put("status", HttpStatus.BAD_REQUEST.value());

	    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(InvalidCurrentPasswordException.class)
	public ResponseEntity<Object> handleInvalidCurrentPassword(InvalidCurrentPasswordException ex, HttpServletRequest request) {
	    Map<String, Object> body = new HashMap<>();
	    body.put("message", ex.getMessage());
	    body.put("path", request.getRequestURI());
	    body.put("status", HttpStatus.UNAUTHORIZED.value());

	    return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<Object> handleBadCredentials(BadCredentialsException ex, HttpServletRequest request) {
	    Map<String, Object> body = new HashMap<>();
	    body.put("message", "Invalid username or password");
	    body.put("path", request.getRequestURI());
	    body.put("status", HttpStatus.UNAUTHORIZED.value());

	    return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(DisabledException.class)
	public ResponseEntity<Object> handleDisabledUser(DisabledException ex, HttpServletRequest request) {
	    Map<String, Object> body = new HashMap<>();
	    body.put("message", "User account is disabled");
	    body.put("path", request.getRequestURI());
	    body.put("status", HttpStatus.UNAUTHORIZED.value());

	    return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
	}
	
	// Handle Bad Request exceptions
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Object> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
	    Map<String, Object> body = new HashMap<>();
	    body.put("message", ex.getMessage());
	    body.put("path", request.getRequestURI());
	    body.put("status", HttpStatus.BAD_REQUEST.value());
	    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}
	
    // Handle Resource Not Found exceptions
	@ExceptionHandler({ResourceNotFoundException.class, EntityNotFoundException.class})
    public ResponseEntity<Object> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", ex.getMessage());
        body.put("path", request.getRequestURI());
        body.put("status", HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    // Handle validation errors (MethodArgumentNotValidException)
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        
        Map<String, Object> body = new HashMap<>();
        body.put("message", "Validation failed");
        body.put("status", status.value());

        // Collect validation errors
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage()));
        
        body.put("errors", errors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // Handle unsupported HTTP methods
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        Map<String, Object> body = new HashMap<>();
        body.put("message", "Method not supported: " + ex.getMethod());
        body.put("status", HttpStatus.METHOD_NOT_ALLOWED.value());

        return new ResponseEntity<>(body, HttpStatus.METHOD_NOT_ALLOWED);
    }

    // Handle 401 Unauthorized (AuthenticationException)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex, HttpServletRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", "Unauthorized: " + ex.getMessage());
        body.put("path", request.getRequestURI());
        body.put("status", HttpStatus.UNAUTHORIZED.value());

        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }

    // Handle 403 Forbidden (AccessDeniedException)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", "Forbidden: Access is denied.");
        body.put("path", request.getRequestURI());
        body.put("status", HttpStatus.FORBIDDEN.value());

        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    // Handle all other unhandled exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllOtherExceptions(Exception ex, HttpServletRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", "An error occurred");
        body.put("details", ex.getMessage());
        body.put("path", request.getRequestURI());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
