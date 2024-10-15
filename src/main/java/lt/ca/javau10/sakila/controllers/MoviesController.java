package lt.ca.javau10.sakila.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lt.ca.javau10.sakila.models.dto.MovieDto;
import lt.ca.javau10.sakila.services.MovieService;

@RestController
@RequestMapping("/api/movies")
public class MoviesController {

	private final MovieService service;

    public MoviesController(MovieService service) {
        this.service = service;
    }

	@GetMapping
	public ResponseEntity<List<MovieDto>> getAllMovies() {
	    List<MovieDto> movies = service.getAllMovies();
	    return ResponseEntity.ok(movies);
	}

	@GetMapping("/{id}")
	public ResponseEntity<MovieDto> getMovieById(@PathVariable Short id) {
	    MovieDto movie = service.getMovieById(id);
	    return ResponseEntity.ok(movie);
	}

}
