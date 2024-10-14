package lt.ca.javau10.sakila.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import lt.ca.javau10.sakila.models.Rental;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Integer> {
	@Query("SELECT r FROM Rental r WHERE r.customer.user.userId = :userId")
	List<Rental> findAllByUserId(@Param("userId") Integer userId);
}