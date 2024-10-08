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
    
    @Column(name = "phone")
    private String phone;
    
    @OneToOne
    @JoinColumn(name = "city_id", referencedColumnName = "city_id")
    private City city;

	public Address() {}

	public Address(String address, String district, String phone, City city) {
		this.address = address;
		this.district = district;
		this.phone = phone;
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
