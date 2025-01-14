package lt.ca.javau10.sakila.repositories;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lt.ca.javau10.sakila.models.Actor;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Short> {
	boolean existsByFirstNameAndLastName(String firstName, String lastName);
	Optional<Actor> findByFirstNameAndLastName(String firstName, String lastName);
}