package lt.ca.javau10.sakila.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import lt.ca.javau10.sakila.entities.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	User findByUsername(String username);
}