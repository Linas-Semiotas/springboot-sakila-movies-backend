package lt.ca.javau10.sakila.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lt.ca.javau10.sakila.dto.RentalDto;
import lt.ca.javau10.sakila.entities.Movie;
import lt.ca.javau10.sakila.repositories.MovieRepository;
import lt.ca.javau10.sakila.utils.Utils;

@Service
public class RentalService {

	@Autowired
    private MovieRepository repository;
	
	public List<RentalDto> getAllRentals() {
		List<Movie> films = repository.findAll();
        return films.stream()
        			.map(this::convertToDto)
                    .collect(Collectors.toList());
	}

	public RentalDto getRentalById(Short id) {
		return repository.findById(id)
				.map(this::convertToDto)
    			.orElse(null);
	}

    
    private RentalDto convertToDto(Movie movie) {
        return new RentalDto(
    		movie.getId(),
    		Utils.capitalize(movie.getTitle()),
    		movie.getRentalRate(),
            movie.getReplacementCost(),
            movie.getRentalDuration()
        );
    }

}
