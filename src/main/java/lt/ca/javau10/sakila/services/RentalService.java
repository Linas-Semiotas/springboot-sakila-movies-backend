package lt.ca.javau10.sakila.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lt.ca.javau10.sakila.exceptions.InsufficientBalanceException;
import lt.ca.javau10.sakila.exceptions.InventoryNotAvailableException;
import lt.ca.javau10.sakila.exceptions.MovieNotFoundException;
import lt.ca.javau10.sakila.exceptions.UserNotFoundException;
import lt.ca.javau10.sakila.models.Inventory;
import lt.ca.javau10.sakila.models.Movie;
import lt.ca.javau10.sakila.models.Rental;
import lt.ca.javau10.sakila.models.User;
import lt.ca.javau10.sakila.models.dto.RentalDto;
import lt.ca.javau10.sakila.repositories.InventoryRepository;
import lt.ca.javau10.sakila.repositories.MovieRepository;
import lt.ca.javau10.sakila.repositories.RentalRepository;
import lt.ca.javau10.sakila.repositories.UserRepository;
import lt.ca.javau10.sakila.utils.Utils;

@Service
public class RentalService {
	
	private final RentalRepository rentalRepository;
	private final InventoryRepository inventoryRepository;
	private final MovieRepository movieRepository;
	private final UserRepository userRepository;
	private final UserService userService;

    public RentalService(RentalRepository rentalRepository,
    		InventoryRepository inventoryRepository,
    		MovieRepository movieRepository,
    		UserRepository userRepository,
    		UserService userService) {
        this.rentalRepository = rentalRepository;
        this.inventoryRepository = inventoryRepository;
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }
	
    @Transactional(readOnly = true)
	public List<RentalDto> getAllRentals() {
        List<Movie> movies = movieRepository.findAll();
        
        return movies.stream()
                .map(movie -> new RentalDto(
                        movie.getFilmId(),
                        Utils.capitalize(movie.getTitle()),
                        movie.getRentalRate(),
                        movie.getReplacementCost(),
                        movie.getRentalDuration()
                ))
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public RentalDto getRentalById(Short id, String username) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException("Movie with ID " + id + " not found"));
        
        Double balance = userService.getUserBalance(username);
        
        return new RentalDto(
                movie.getFilmId(),
                Utils.capitalize(movie.getTitle()),
                movie.getRentalRate(),
                movie.getReplacementCost(),
                movie.getRentalDuration(),
                balance
        );
    }
    
    @Transactional
    public void rentMovie(Short movieId, String username) {
    	if (movieId == null) {
            throw new IllegalArgumentException("Movie ID must not be null");
        }
    	
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new MovieNotFoundException("Movie not found"));
        
        BigDecimal rentalRate = movie.getRentalRate();
        BigDecimal balance = BigDecimal.valueOf(user.getBalance());
        
        if (balance.compareTo(rentalRate) < 0) {
            throw new InsufficientBalanceException("Insufficient balance to rent the movie.");
        }

        Optional<Inventory> inventoryOpt = inventoryRepository.findFirstByMovieFilmIdAndStoreStoreId(
                movie.getFilmId(), user.getCustomer().getStoreId());

        if (inventoryOpt.isEmpty()) {
            throw new InventoryNotAvailableException("No available inventory for this movie.");
        }

        Inventory inventory = inventoryOpt.get();

        Rental rental = new Rental();
        rental.setRentalDate(LocalDateTime.now());
        rental.setInventory(inventory);
        rental.setCustomer(user.getCustomer());
        rental.setStaffId(user.getCustomer().getStoreId());
        rental.setReturnDate(LocalDateTime.now().plusDays(movie.getRentalDuration()));

        rentalRepository.save(rental);

        user.setBalance(balance.subtract(rentalRate).doubleValue());
        userRepository.save(user);
    }
}
