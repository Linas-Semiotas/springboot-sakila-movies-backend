package lt.ca.javau10.sakila.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lt.ca.javau10.sakila.dto.MovieDto;
import lt.ca.javau10.sakila.entities.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Short> {
    Optional<Movie> findById(Short id);
    
    List<MovieDto> findByDescriptionContaining(String description);
}
