package lt.ca.javau10.sakila.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name="film")
public class Movie {

	@Id
    @Column(name = "film_id")
    private Short id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "release_year")
    private Integer releaseYear;
    
    @Column(name = "length")
    private Short filmLength;

    @Enumerated(EnumType.STRING)
    @Column(name = "rating")
    private Rating rating;
    
    @Column(name = "rental_rate")
    private BigDecimal rentalRate;
    
    @Column(name = "replacement_cost")
    private BigDecimal replacementCost;
    
    @Column(name = "rental_duration")
    private Short rentalDuration;

	@ManyToOne
    @JoinColumn(name = "language_id")
    private Language language;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(
        name = "film_category",
        joinColumns = @JoinColumn(name = "film_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Category category;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "film_actor",
        joinColumns = @JoinColumn(name = "film_id"),
        inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
	private List<Actor> actors;

	public Movie() {}

	public Short getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Integer getReleaseYear() { return releaseYear; }
    public Short getFilmLength() { return filmLength; }
    public Rating getRating() { return rating; }
    public BigDecimal getRentalRate() { return rentalRate; }
    public BigDecimal getReplacementCost() { return replacementCost; }
    public Short getRentalDuration() { return rentalDuration; }
    public Language getLanguage() { return language; }
    public Category getCategory() { return category; }
    public List<Actor> getActor() { return actors; }

    public void setId(Short id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setReleaseYear(Integer releaseYear) { this.releaseYear = releaseYear; }
    public void setFilmLength(Short filmLength) { this.filmLength = filmLength; }
    public void setRating(Rating rating) { this.rating = rating; }
    public void setRentalRate(BigDecimal rentalRate) { this.rentalRate = rentalRate; }
    public void setReplacementCost(BigDecimal replacementCost) { this.replacementCost = replacementCost; }
    public void setRentalDuration(Short rentalDuration) { this.rentalDuration = rentalDuration; }
    public void setLanguage(Language language) { this.language = language; }
    public void setCategory(Category category) { this.category = category; }
    public void setActors(List<Actor> actors) { this.actors = actors != null ? actors : new ArrayList<>(); }
}