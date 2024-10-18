package lt.ca.javau10.sakila.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name="film")
public class Movie {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "film_id")
    private Short filmId;

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
    
    @Column(name = "rental_rate", nullable = false)
    private BigDecimal rentalRate;
    
    @Column(name = "replacement_cost", nullable = false)
    private BigDecimal replacementCost;
    
    @Column(name = "rental_duration", nullable = false)
    private Short rentalDuration;
    
    @Column(name = "special_features")
    private String specialFeatures;

	@ManyToOne
    @JoinColumn(name = "language_id")
    private Language language;
    
	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "film_category",
        joinColumns = @JoinColumn(name = "film_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
	private List<Category> category;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "film_actor",
        joinColumns = @JoinColumn(name = "film_id"),
        inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
	private List<Actor> actors;

	public Movie() {}

	public Movie(Short filmId, String title, String description, Integer releaseYear, Short filmLength, Rating rating,
			BigDecimal rentalRate, BigDecimal replacementCost, Short rentalDuration, String specialFeatures,
			Language language, List<Category> category, List<Actor> actors) {
		this.filmId = filmId;
		this.title = title;
		this.description = description;
		this.releaseYear = releaseYear;
		this.filmLength = filmLength;
		this.rating = rating;
		this.rentalRate = rentalRate;
		this.replacementCost = replacementCost;
		this.rentalDuration = rentalDuration;
		this.specialFeatures = specialFeatures;
		this.language = language;
		this.category = category;
		this.actors = actors;
	}

	public Short getFilmId() { return filmId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Integer getReleaseYear() { return releaseYear; }
    public Short getFilmLength() { return filmLength; }
    public Rating getRating() { return rating; }
    public BigDecimal getRentalRate() { return rentalRate; }
    public BigDecimal getReplacementCost() { return replacementCost; }
    public Short getRentalDuration() { return rentalDuration; }
    public String getSpecialFeatures() { return specialFeatures; }
    public Language getLanguage() { return language; }
    public List<Category> getCategory() { return category; }
    public List<Actor> getActor() { return actors; }

    public void setFilmId(Short filmId) { this.filmId = filmId; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setReleaseYear(Integer releaseYear) { this.releaseYear = releaseYear; }
    public void setFilmLength(Short filmLength) { this.filmLength = filmLength; }
    public void setRating(Rating rating) { this.rating = rating; }
    public void setRentalRate(BigDecimal rentalRate) { this.rentalRate = rentalRate; }
    public void setReplacementCost(BigDecimal replacementCost) { this.replacementCost = replacementCost; }
    public void setRentalDuration(Short rentalDuration) { this.rentalDuration = rentalDuration; }
    public void setSpecialFeatures(String specialFeatures) { this.specialFeatures = specialFeatures; }
    public void setLanguage(Language language) { this.language = language; }
    public void setCategory(List<Category> category) { this.category = category != null ? category : new ArrayList<>(); }
    public void setActors(List<Actor> actors) { this.actors = actors != null ? actors : new ArrayList<>(); }
}