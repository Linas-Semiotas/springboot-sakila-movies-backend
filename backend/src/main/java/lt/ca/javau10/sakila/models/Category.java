package lt.ca.javau10.sakila.models;

import jakarta.persistence.*;

@Entity
@Table(name = "category")
public class Category {
	
	@Id
    @Column(name = "category_id")
    private Byte categoryId;
    @Column(name = "name")
    private String name;
    
	public Byte getCategoryId() {
		return categoryId;
	}
	public String getName() {
		return name;
	}
	public void setCategoryId(Byte categoryId) {
		this.categoryId = categoryId;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}