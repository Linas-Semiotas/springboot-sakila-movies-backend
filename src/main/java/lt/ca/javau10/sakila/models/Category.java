package lt.ca.javau10.sakila.models;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "category")
public class Category {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Byte categoryId;
	
    @Column(name = "name")
    private String name;
    
    @ManyToMany(mappedBy = "category")
    private List<Movie> films;
    
	public Category() {}
	
	public Category(String name) {
		this.name = name;
	}

	public Byte getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Byte categoryId) {
		this.categoryId = categoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public List<Movie> getFilms() {
        return films;
    }

    public void setFilms(List<Movie> films) {
        this.films = films;
    }
}