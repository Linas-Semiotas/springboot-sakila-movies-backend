package lt.ca.javau10.sakila.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lt.ca.javau10.sakila.models.Actor;
import lt.ca.javau10.sakila.models.Category;
import lt.ca.javau10.sakila.models.Language;
import lt.ca.javau10.sakila.models.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Short> {
    Optional<Movie> findById(Short id);
    boolean existsByLanguage(Language language);
	boolean existsByCategory(Category category);
	boolean existsByActorsContaining(Actor actor);
}
