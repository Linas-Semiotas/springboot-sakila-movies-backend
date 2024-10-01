package lt.ca.javau10.sakila.dto;

import java.math.BigDecimal;

public class RentalDto {
	private Short id;
    private String title;
    private BigDecimal rentalRate;
    private BigDecimal replacementCost;
    private Short rentalDuration;
    
	public RentalDto() {}
	
	public RentalDto(Short id, String title, BigDecimal rentalRate, BigDecimal replacementCost, Short rentalDuration) {
		this.id = id;
		this.title = title;
		this.rentalRate = rentalRate;
		this.replacementCost = replacementCost;
		this.rentalDuration = rentalDuration;
	}

	public Short getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public BigDecimal getRentalRate() {
		return rentalRate;
	}

	public BigDecimal getReplacementCost() {
		return replacementCost;
	}

	public Short getRentalDuration() {
		return rentalDuration;
	}
}
