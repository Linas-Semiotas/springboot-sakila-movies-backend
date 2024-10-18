package lt.ca.javau10.sakila.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Short addressId;

    @Column(name = "address")
    private String address;

    @Column(name = "district", nullable = false)
    private String district;
    
    @Column(name = "phone")
    private String phone;
    
    @Column(name = "postal_code")
    private String postalCode;
    
    @OneToOne
    @JoinColumn(name = "city_id", referencedColumnName = "city_id")
    private City city;
    
    @Column(name = "location", columnDefinition = "geometry")
    private Geometry location;

	public Address() {}

	public Address(String address, String district, String phone, String postalCode, City city) {
		this.address = address;
		this.district = district;
		this.phone = phone;
		this.postalCode = postalCode;
		this.city = city;
		this.location = createDefaultLocation();
	}
	
	private static Geometry createDefaultLocation() {
        GeometryFactory geometryFactory = new GeometryFactory();
        return geometryFactory.createPoint(new Coordinate(0, 0));
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

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
