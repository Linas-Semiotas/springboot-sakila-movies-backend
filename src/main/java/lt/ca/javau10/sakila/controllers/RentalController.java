package lt.ca.javau10.sakila.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lt.ca.javau10.sakila.models.dto.MovieDto;
import lt.ca.javau10.sakila.models.dto.RentalDto;
import lt.ca.javau10.sakila.security.responses.MessageResponse;
import lt.ca.javau10.sakila.services.RentalService;

@RestController
@RequestMapping("/api/rental")
public class RentalController {
    
    private final RentalService service;

    public RentalController(RentalService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<RentalDto>> getAllRentals() {
        List<RentalDto> rentals = service.getAllRentals();
        return ResponseEntity.ok(rentals);
    }
    
    @PostMapping("/rent")
    public ResponseEntity<MessageResponse> rentMovie(@RequestBody MovieDto movieDto, Principal principal) {
        String username = principal.getName();
        try {
            service.rentMovie(movieDto.getId(), username);
            return ResponseEntity.ok(new MessageResponse("Movie rented successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRentalById(@PathVariable Short id, Principal principal) {
        String username = principal.getName();
        RentalDto rentalDto = service.getRentalById(id, username);
        return ResponseEntity.ok(rentalDto);
    }
}