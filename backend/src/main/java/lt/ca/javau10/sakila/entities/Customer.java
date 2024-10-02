package lt.ca.javau10.sakila.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Short customerId;
    
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @OneToOne
    @JoinColumn(name = "address_id")
    private Address address;
    
    private String firstName;
    private String lastName;
    private String email;
    private Byte storeId;

    public Customer() {}
    
    public Customer(String firstName, String lastName, String email, Byte storeId, Address address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.storeId = storeId;
        this.address = address;
    }

	public Short getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Short customerId) {
		this.customerId = customerId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Byte getStoreId() {
		return storeId;
	}

	public void setStoreId(Byte storeId) {
		this.storeId = storeId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
}