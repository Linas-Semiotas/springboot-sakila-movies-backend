package lt.ca.javau10.sakila.models.dto;

public class StoreDto {

	private Byte storeId;
	private String address;
    private String city;
    private String country;

    public StoreDto() {}

    public StoreDto(Byte storeId, String country, String city, String address) {
        this.storeId = storeId;
        this.country = country;
        this.city = city;
        this.address = address;
    }

	public Byte getStoreId() {
		return storeId;
	}
	
	public String getCountry() {
		return country;
	}
	
	public String getCity() {
		return city;
	}
	
	public String getAddress() {
		return address;
	}
}
