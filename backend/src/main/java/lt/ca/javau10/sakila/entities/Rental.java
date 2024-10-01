package lt.ca.javau10.sakila.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="film")
public class Rental {

	@Id
    @Column(name = "film_id")
    private Short id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "release_year")
    private Integer releaseYear;

    public Rental() {}
    
	public Rental(Short id, String title, String description, Integer releaseYear) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.releaseYear = releaseYear;
	}

	public Short getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public Integer getReleaseYear() {
		return releaseYear;
	}
}

