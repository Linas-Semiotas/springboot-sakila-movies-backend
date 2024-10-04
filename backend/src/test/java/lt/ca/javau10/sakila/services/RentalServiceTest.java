package lt.ca.javau10.sakila.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import lt.ca.javau10.sakila.models.Movie;
import lt.ca.javau10.sakila.models.dto.RentalDto;
import lt.ca.javau10.sakila.repositories.MovieRepository;

public class RentalServiceTest {

    @InjectMocks
    private RentalService rentalService;

    @Mock
    private MovieRepository movieRepository;

    private Movie movie1;
    private Movie movie2;
    private RentalDto rentalDto1;
    private RentalDto rentalDto2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create sample Movie objects
        movie1 = new Movie();
        movie1.setId((short) 1);
        movie1.setTitle("Inception");
        movie1.setRentalRate(BigDecimal.valueOf(2.99));
        movie1.setReplacementCost(BigDecimal.valueOf(19.99));
        movie1.setRentalDuration((short) 5);

        movie2 = new Movie();
        movie2.setId((short) 2);
        movie2.setTitle("The Matrix");
        movie2.setRentalRate(BigDecimal.valueOf(3.49));
        movie2.setReplacementCost(BigDecimal.valueOf(24.99));
        movie2.setRentalDuration((short) 7);

        // Create corresponding RentalDto objects
        rentalDto1 = new RentalDto(movie1.getId(), movie1.getTitle(), movie1.getRentalRate(), movie1.getReplacementCost(), movie1.getRentalDuration());
        rentalDto2 = new RentalDto(movie2.getId(), movie2.getTitle(), movie2.getRentalRate(), movie2.getReplacementCost(), movie2.getRentalDuration());
    }

    @Test
    public void testGetAllRentals() {
        // Arrange: Mock the repository to return a list of movies
        when(movieRepository.findAll()).thenReturn(Arrays.asList(movie1, movie2));

        // Act: Call the service method
        List<RentalDto> rentalDtos = rentalService.getAllRentals();

        // Assert: Verify the results
        assertNotNull(rentalDtos);
        assertEquals(2, rentalDtos.size());

        // Verify the details of the first rental
        assertEquals(rentalDto1.getId(), rentalDtos.get(0).getId());
        assertEquals(rentalDto1.getTitle(), rentalDtos.get(0).getTitle());
        assertEquals(rentalDto1.getRentalRate(), rentalDtos.get(0).getRentalRate());
        assertEquals(rentalDto1.getReplacementCost(), rentalDtos.get(0).getReplacementCost());
        assertEquals(rentalDto1.getRentalDuration(), rentalDtos.get(0).getRentalDuration());

        // Verify the details of the second rental
        assertEquals(rentalDto2.getId(), rentalDtos.get(1).getId());
        assertEquals(rentalDto2.getTitle(), rentalDtos.get(1).getTitle());
        assertEquals(rentalDto2.getRentalRate(), rentalDtos.get(1).getRentalRate());
        assertEquals(rentalDto2.getReplacementCost(), rentalDtos.get(1).getReplacementCost());
        assertEquals(rentalDto2.getRentalDuration(), rentalDtos.get(1).getRentalDuration());

        // Verify that the repository was called once
        verify(movieRepository, times(1)).findAll();
    }

    @Test
    public void testGetRentalById_Found() {
        // Arrange: Mock the repository to return a movie by ID
        when(movieRepository.findById((short) 1)).thenReturn(Optional.of(movie1));

        // Act: Call the service method
        RentalDto rentalDto = rentalService.getRentalById((short) 1);

        // Assert: Verify the result
        assertNotNull(rentalDto);
        assertEquals(rentalDto1.getId(), rentalDto.getId());
        assertEquals(rentalDto1.getTitle(), rentalDto.getTitle());
        assertEquals(rentalDto1.getRentalRate(), rentalDto.getRentalRate());
        assertEquals(rentalDto1.getReplacementCost(), rentalDto.getReplacementCost());
        assertEquals(rentalDto1.getRentalDuration(), rentalDto.getRentalDuration());

        // Verify that the repository was called once
        verify(movieRepository, times(1)).findById((short) 1);
    }

    @Test
    public void testGetRentalById_NotFound() {
        // Arrange: Mock the repository to return an empty Optional
        when(movieRepository.findById((short) 1)).thenReturn(Optional.empty());

        // Act: Call the service method
        RentalDto rentalDto = rentalService.getRentalById((short) 1);

        // Assert: Verify the result
        assertNull(rentalDto);

        // Verify that the repository was called once
        verify(movieRepository, times(1)).findById((short) 1);
    }
}
