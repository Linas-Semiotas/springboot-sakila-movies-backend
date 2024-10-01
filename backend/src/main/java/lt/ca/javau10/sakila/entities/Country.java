package lt.ca.javau10.sakila.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "country")
public class Country {

    @Id
    @Column(name = "country_id")
    private Short countryId;

    @Column(name = "country")
    private String country;

	public Short getCountryId() {
		return countryId;
	}

	public String getCountry() {
		return country;
	}
}