package lt.ca.javau10.sakila.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "city")
public class City {

    @Id
    @Column(name = "city_id")
    private Short cityId;

    @Column(name = "city")
    private String city;

    @OneToOne
    @JoinColumn(name = "country_id")
    private Country country;

	public City() {}

	public City(String city, Country country) {
		this.city = city;
		this.country = country;
	}

	public Short getCityId() {
		return cityId;
	}

	public void setCityId(Short cityId) {
		this.cityId = cityId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	   
}