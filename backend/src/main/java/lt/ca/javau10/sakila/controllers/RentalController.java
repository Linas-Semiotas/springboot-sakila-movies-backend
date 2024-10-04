package lt.ca.javau10.sakila.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lt.ca.javau10.sakila.models.dto.RentalDto;
import lt.ca.javau10.sakila.services.RentalService;

@RestController
@RequestMapping("/api/rental")
public class RentalController {

    @Autowired
    private RentalService service;

    @GetMapping
    public List<RentalDto> getAllRentals() {
        return service.getAllRentals();
    }

    @GetMapping("/{id}")
    public RentalDto getRentalById(@PathVariable Short id) {
        return service.getRentalById(id);
    }
}
