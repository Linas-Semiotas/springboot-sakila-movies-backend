package lt.ca.javau10.sakila.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import lt.ca.javau10.sakila.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Short> {
	
}