package lt.ca.javau10.sakila.models.dto;

public class StoreDto {
	private Byte storeId;
	private String address;
    private String city;
    private String country;
    
    public StoreDto() {}
    
	public StoreDto(Byte storeId, String address, String city, String country) {
		this.storeId = storeId;
		this.address = address;
		this.city = city;
		this.country = country;
	}

	public Byte getStoreId() {
		return storeId;
	}

	public void setStoreId(Byte storeId) {
		this.storeId = storeId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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
