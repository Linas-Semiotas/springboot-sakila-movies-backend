package lt.ca.javau10.sakila.models;

import jakarta.persistence.*;

@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Short addressId;

    @Column(name = "address")
    private String address;

    @Column(name = "district")
    private String district;
    
    @OneToOne
    @JoinColumn(name = "city_id", referencedColumnName = "city_id")
    private City city;

	public Address() {}

	public Address(String address, String district, City city) {
		this.address = address;
		this.district = district;
		this.city = city;
	}

	public Short getAddressId() {
		return addressId;
	}

	public void setAddressId(Short addressId) {
		this.addressId = addressId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
