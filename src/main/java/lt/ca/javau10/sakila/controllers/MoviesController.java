package lt.ca.javau10.sakila.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lt.ca.javau10.sakila.models.dto.MovieDto;
import lt.ca.javau10.sakila.services.MovieService;

@RestController
@RequestMapping("/api/movies")
public class MoviesController {

	@Autowired
    private MovieService service;

    @GetMapping
    public List<MovieDto> getAllMovies() {
        return service.getAllMovies();
    }

    @GetMapping("/{id}")
    public MovieDto getMovieById(@PathVariable Short id) {
        return service.getMovieById(id);
    }
}
