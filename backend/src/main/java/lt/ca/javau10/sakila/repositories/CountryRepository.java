package lt.ca.javau10.sakila.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lt.ca.javau10.sakila.models.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Short> {
	Optional<Country> findByCountry(String country);
}
