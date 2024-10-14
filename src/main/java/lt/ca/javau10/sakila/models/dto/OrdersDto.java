package lt.ca.javau10.sakila.models.dto;

import java.time.LocalDateTime;

public class OrdersDto {
	private Integer id;
	private LocalDateTime rentalDate;
	private LocalDateTime returnDate;
	private String title;
	
	public OrdersDto() {}
	
	public OrdersDto(Integer id, LocalDateTime rentalDate, LocalDateTime returnDate, String title) {
		this.id = id;
		this.rentalDate = rentalDate;
		this.returnDate = returnDate;
		this.title = title;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDateTime getRentalDate() {
		return rentalDate;
	}

	public void setRentalDate(LocalDateTime rentalDate) {
		this.rentalDate = rentalDate;
	}

	public LocalDateTime getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(LocalDateTime returnDate) {
		this.returnDate = returnDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
