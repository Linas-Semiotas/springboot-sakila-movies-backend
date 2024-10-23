package lt.ca.javau10.sakila.models.dto;

import jakarta.validation.constraints.Pattern;

public class AddressInfoDto {

	@Pattern(regexp = "^$|.{5,100}", message = "Address must be between 5 and 100 characters")
    private String address;

    @Pattern(regexp = "^$|.{2,50}", message = "District must be between 2 and 50 characters")
    private String district;

    @Pattern(regexp = "^[A-Za-z0-9\\s]{3,10}$|^$", message = "Postal code is invalid")
    private String postalCode;

    @Pattern(regexp = "^$|.{2,50}", message = "City must be between 2 and 50 characters")
    private String city;

    @Pattern(regexp = "^$|.{2,50}", message = "Country must be between 2 and 50 characters")
    private String country;

    public AddressInfoDto() {}

    public AddressInfoDto(String address, String district, String postalCode, String city, String country) {
        this.address = address;
        this.district = district;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
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

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
