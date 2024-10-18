package lt.ca.javau10.sakila.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lt.ca.javau10.sakila.models.dto.StoreDto;
import lt.ca.javau10.sakila.services.StoreService;

@RestController
@RequestMapping("/api/stores")
public class StoresController {
	
	private final StoreService service;

    public StoresController(StoreService service) {
        this.service = service;
    }

	@GetMapping
    public ResponseEntity<?> getAllStores() {
        List<StoreDto> stores = service.getAllStores();
        return ResponseEntity.ok(stores);
    }
}
