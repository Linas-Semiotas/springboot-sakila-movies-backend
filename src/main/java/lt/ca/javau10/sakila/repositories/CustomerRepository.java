package lt.ca.javau10.sakila.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lt.ca.javau10.sakila.models.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Short> {
	Optional<Customer> findByUser_UserId(int userId);
}