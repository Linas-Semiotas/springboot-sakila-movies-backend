package lt.ca.javau10.sakila.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import lt.ca.javau10.sakila.models.dto.StoreDto;
import lt.ca.javau10.sakila.services.StoreService;

@RestController
@RequestMapping("/api/stores")
public class StoresController {
	@Autowired
    private StoreService service;

    @GetMapping
    public List<StoreDto> getAllStores() {
        return service.getAllStores();
    }
}
