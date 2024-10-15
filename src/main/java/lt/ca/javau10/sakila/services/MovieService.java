package lt.ca.javau10.sakila.services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lt.ca.javau10.sakila.exceptions.MovieNotFoundException;
import lt.ca.javau10.sakila.models.Movie;
import lt.ca.javau10.sakila.models.dto.MovieDto;
import lt.ca.javau10.sakila.repositories.MovieRepository;
import lt.ca.javau10.sakila.utils.Utils;

@Service
public class MovieService {
    
    private final MovieRepository repository;

    public MovieService(MovieRepository repository) {
        this.repository = repository;
    }
    
    public List<MovieDto> getAllMovies() {
        return repository.findAll().stream()
        		.map(this::convertToDto)
            	.collect(Collectors.toList());
    }

	public MovieDto getMovieById(Short id) {
        return repository.findById(id)
				.map(this::convertToDto)
				.orElseThrow(() -> new MovieNotFoundException("Movie with ID " + id + " not found"));
	}
    
    private MovieDto convertToDto(Movie movie) {
    	List<String> category = Optional.ofNullable(movie.getCategory())
            .map(cats -> cats.stream()
        		.map(categ -> Utils.capitalize(categ.getName()))
                .collect(Collectors.toList()))
            .orElse(Collections.emptyList());

        List<String> actors = Optional.ofNullable(movie.getActor())
            .map(acts -> acts.stream()
        		.map(actor -> Utils.capitalize(actor.getFirstName()) + " " + Utils.capitalize(actor.getLastName()))
                .collect(Collectors.toList()))
            .orElse(Collections.emptyList());

	    return new MovieDto(
	        movie.getFilmId(),
	        Utils.capitalize(movie.getTitle()),
	        movie.getDescription(),
	        movie.getReleaseYear(),
	        movie.getFilmLength(),
	        movie.getRating(),            
	        movie.getLanguage() != null ? movie.getLanguage().getName() : null,
			category,
	        movie.getSpecialFeatures(),
	        actors
	    );
    }
}
