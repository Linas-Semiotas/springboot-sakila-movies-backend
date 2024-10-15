package lt.ca.javau10.sakila.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	@Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserRepository userRepository;
	
	@Autowired
    private UserService userService;
	
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
    
    public RentalDto getRentalById(Short id, String username) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rental not found"));
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
    
    public String rentMovie(Short movieId, String username) {
    	if (movieId == null) {
            throw new IllegalArgumentException("Movie ID must not be null");
        }
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        BigDecimal rentalRate = movie.getRentalRate();
        double balance = user.getBalance();
        if (BigDecimal.valueOf(balance).compareTo(rentalRate) < 0) {
            return "Insufficient balance.";
        }

        Optional<Inventory> inventoryOpt = inventoryRepository.findFirstByMovieFilmIdAndStoreStoreId(
                movie.getFilmId(), user.getCustomer().getStoreId());

        if (inventoryOpt.isEmpty()) {
            return "No available inventory for this movie.";
        }

        Inventory inventory = inventoryOpt.get();

        Rental rental = new Rental();
        rental.setRentalDate(LocalDateTime.now());
        rental.setInventory(inventory);
        rental.setCustomer(user.getCustomer());
        rental.setStaffId(user.getCustomer().getStoreId());
        rental.setReturnDate(LocalDateTime.now().plusDays(movie.getRentalDuration()));

        rentalRepository.save(rental);

        user.setBalance(user.getBalance() - rentalRate.doubleValue());
        userRepository.save(user);
        
        return "Rental successful!";
    }
}
