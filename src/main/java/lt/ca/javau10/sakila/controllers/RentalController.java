package lt.ca.javau10.sakila.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import lt.ca.javau10.sakila.services.RentalService;

@RestController
@RequestMapping("/api/rental")
public class RentalController {

    @Autowired
    private RentalService service;

    @GetMapping
    public ResponseEntity<List<RentalDto>> getAllRentals() {
        List<RentalDto> rentals = service.getAllRentals();
        return new ResponseEntity<>(rentals, HttpStatus.OK);
    }
    
    @PostMapping("/rent")
    public ResponseEntity<String> rentMovie(@RequestBody MovieDto MovieDto, Principal principal) {
        String username = principal.getName();
        String result = service.rentMovie(MovieDto.getId(), username);
        
        if ("Rental successful!".equals(result)) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentalDto> getRentalById(@PathVariable Short id, Principal principal) {
        try {
            String username = principal.getName();
            RentalDto rentalDto = service.getRentalById(id, username);
            return new ResponseEntity<>(rentalDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}