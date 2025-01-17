package lt.ca.javau10.sakila.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lt.ca.javau10.sakila.models.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Short> {
	
}
