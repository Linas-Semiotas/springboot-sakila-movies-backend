package lt.ca.javau10.sakila.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "country")
public class Country {

    @Id
    @Column(name = "country_id")
    private Short countryId;

    @Column(name = "country")
    private String country;

 
	public Country() {}

	public Country(String country) {
		this.country = country;
	}
	
	public Short getCountryId() {
		return countryId;
	}

	public void setCountryId(Short countryId) {
		this.countryId = countryId;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	
}