package lt.ca.javau10.sakila.models.dto;

import java.math.BigDecimal;

public class RentalDto {
	private Short id;
    private String title;
    private BigDecimal rentalRate;
    private BigDecimal replacementCost;
    private Short rentalDuration;
    private Double balance;
    
	public RentalDto() {}
	
	public RentalDto(Short id, String title, BigDecimal rentalRate, BigDecimal replacementCost, Short rentalDuration) {
		this.id = id;
		this.title = title;
		this.rentalRate = rentalRate;
		this.replacementCost = replacementCost;
		this.rentalDuration = rentalDuration;
	}
	
	public RentalDto(Short id, String title, BigDecimal rentalRate, BigDecimal replacementCost, Short rentalDuration, Double balance) {
		this.id = id;
		this.title = title;
		this.rentalRate = rentalRate;
		this.replacementCost = replacementCost;
		this.rentalDuration = rentalDuration;
		this.balance = balance;
	}

	public Short getId() {
		return id;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public BigDecimal getRentalRate() {
		return rentalRate;
	}

	public void setRentalRate(BigDecimal rentalRate) {
		this.rentalRate = rentalRate;
	}

	public BigDecimal getReplacementCost() {
		return replacementCost;
	}

	public void setReplacementCost(BigDecimal replacementCost) {
		this.replacementCost = replacementCost;
	}

	public Short getRentalDuration() {
		return rentalDuration;
	}

	public void setRentalDuration(Short rentalDuration) {
		this.rentalDuration = rentalDuration;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}
}
