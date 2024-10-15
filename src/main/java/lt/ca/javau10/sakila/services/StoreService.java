package lt.ca.javau10.sakila.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lt.ca.javau10.sakila.models.Store;
import lt.ca.javau10.sakila.models.dto.StoreDto;
import lt.ca.javau10.sakila.repositories.StoreRepository;

@Service
public class StoreService {
	
	private final StoreRepository repository;

    public StoreService(StoreRepository repository) {
        this.repository = repository;
    }

    public List<StoreDto> getAllStores() {
        List<Store> stores = repository.findAll();
        
        return stores.stream()
                 .map(store -> new StoreDto(
                		store.getStoreId(),
            			store.getAddress().getCity().getCountry().getCountry(),
            			store.getAddress().getCity().getCity(),
            			store.getAddress().getAddress() 
        		 ))
                 .collect(Collectors.toList());
    }
}
