package lt.ca.javau10.sakila.repositories;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lt.ca.javau10.sakila.models.Language;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Short> {
    boolean existsByName(String name);
    Optional<Language> findByName(String name);
}