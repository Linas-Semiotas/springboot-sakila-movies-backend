package lt.ca.javau10.sakila.models.dto;

public class AddressInfoDto {

    private String address;
    private String district;
    private String postalCode;
    private String city;
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
