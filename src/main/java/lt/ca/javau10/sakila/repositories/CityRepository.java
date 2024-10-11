package lt.ca.javau10.sakila.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lt.ca.javau10.sakila.models.City;
import lt.ca.javau10.sakila.models.Country;

@Repository
public interface CityRepository extends JpaRepository<City, Short> {
	Optional<City> findByCity(String city);
	boolean existsByCountry(Country country);
}
