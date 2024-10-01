package lt.ca.javau10.sakila.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
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

	public Address getAddress() {
		return address;
	}
}

