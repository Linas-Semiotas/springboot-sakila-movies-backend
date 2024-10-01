package lt.ca.javau10.sakila.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lt.ca.javau10.sakila.dto.StoreDto;
import lt.ca.javau10.sakila.entities.Store;
import lt.ca.javau10.sakila.repositories.StoreRepository;

@Service
public class StoreService {

	@Autowired
    private StoreRepository repository;

	private StoreDto convertToDto(Store store) {
        return new StoreDto(
            store.getStoreId(),
            store.getAddress().getCity().getCountry().getCountry(),
            store.getAddress().getCity().getCity(),
            store.getAddress().getAddress()
        );
    }

    public List<StoreDto> getAllStores() {
        List<Store> stores = repository.findAll();
        return stores.stream()
                     .map(this::convertToDto)
                     .collect(Collectors.toList());
    }
}
