package lt.ca.javau10.sakila.entities;

import jakarta.persistence.*;

@Entity
@Table(name="store")
public class Store {

	@Id
    @Column(name = "store_id")
    private Byte storeId;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;

	public Byte getStoreId() {
		return storeId;
	}
	
	public void setStoreId(Byte storeId) {
		this.storeId = storeId;
	}

	public Address getAddress() {
		return address;
	}
	
	public void setAddress(Address address) {
		this.address = address;
	}
}

